package com.fabricio.admin.catalogo.infrastructure.video;

import com.fabricio.admin.catalogo.domain.Identifier;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.video.*;
import com.fabricio.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fabricio.admin.catalogo.infrastructure.services.EventService;
import com.fabricio.admin.catalogo.infrastructure.utils.SqlUtils;
import com.fabricio.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.fabricio.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.fabricio.admin.catalogo.domain.utils.CollectionUtils.mapTo;
import static com.fabricio.admin.catalogo.domain.utils.CollectionUtils.nullIfEmpty;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;
    private final EventService eventService;

    public DefaultVideoGateway(
            final VideoRepository videoRepository,
            @VideoCreatedQueue final EventService eventService
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId = anId.getValue();
        if (videoRepository.existsById(aVideoId)) {
            videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    /**
     * 1. Teste para ordenação asc desc e multiplos params
     * 2. Teste para paginação
     * 3. Teste para quando não tem videos persistidos
     * 4. Teste para busca por termo
     * 5. Teste para busca por castMember
     * 6. Teste para busca por categoria
     * 7. Teste para busca por genero
     * 8. Teste buscando com todos os params
     * 9. Teste buscando videos que não possuem relacionamento
     *
     * @param aQuery
     * @return
     */
    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {

        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = videoRepository.findAll(
                SqlUtils.upper(SqlUtils.like(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );
        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(Video aVideo) {
        final var result = videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();

        aVideo.publishDomainEvents(this.eventService::send);

        return result;
    }
}
