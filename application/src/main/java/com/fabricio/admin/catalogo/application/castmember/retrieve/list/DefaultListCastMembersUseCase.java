package com.fabricio.admin.catalogo.application.castmember.retrieve.list;

import com.fabricio.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Objects;

public non-sealed class DefaultListCastMembersUseCase extends ListCastMembersUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultListCastMembersUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(final SearchQuery aQuery) {
        return castMemberGateway.findAll(aQuery)
                .map(CastMemberListOutput::from);
    }
}
