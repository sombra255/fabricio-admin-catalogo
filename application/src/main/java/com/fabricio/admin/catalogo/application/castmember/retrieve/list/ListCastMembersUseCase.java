package com.fabricio.admin.catalogo.application.castmember.retrieve.list;

import com.fabricio.admin.catalogo.application.UseCase;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMembersUseCase {
}
