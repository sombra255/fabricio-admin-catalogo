package com.fabricio.admin.catalogo.application.video.retrieve.get;


import com.fabricio.admin.catalogo.domain.exceptions.NotFoundException;
import com.fabricio.admin.catalogo.domain.video.Video;
import com.fabricio.admin.catalogo.domain.video.VideoGateway;
import com.fabricio.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anIn) {
        final var aVideoId = VideoID.from(anIn);
        return this.videoGateway.findById(aVideoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
    }
}
