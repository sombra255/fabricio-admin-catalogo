package com.fabricio.admin.catalogo;

import com.fabricio.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.fabricio.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fabricio.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.fabricio.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);
//        final var repositories = appContext
//                .getBeansOfType(CrudRepository.class)
//                .values();

        cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(CastMemberRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));
//
//        final var em = appContext.getBean(TestEntityManager.class);
//        em.flush();
//        em.clear();
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
