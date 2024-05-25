package com.fabricio.admin.catalogo.application.category.retrieve.list;

import com.fabricio.admin.catalogo.application.UseCase;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
