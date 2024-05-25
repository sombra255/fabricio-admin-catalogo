package com.fabricio.admin.catalogo.application.video.retrieve.list;

import com.fabricio.admin.catalogo.application.UseCase;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
