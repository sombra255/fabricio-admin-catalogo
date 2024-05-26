package com.fabricio.admin.catalogo.application.genre.retrieve.list;

import com.fabricio.admin.catalogo.application.UseCase;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
