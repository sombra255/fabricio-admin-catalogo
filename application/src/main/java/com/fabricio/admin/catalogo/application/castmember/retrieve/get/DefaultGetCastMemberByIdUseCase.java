package com.fabricio.admin.catalogo.application.castmember.retrieve.get;

import com.fabricio.admin.catalogo.domain.castmember.CastMember;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberID;
import com.fabricio.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String anIn) {
        final var aCastMemberId = CastMemberID.from(anIn);
        return castMemberGateway.findById(aCastMemberId)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, aCastMemberId));
    }
}
