package com.fabricio.admin.catalogo.application.castmember.create;

import com.fabricio.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
        String name,
        CastMemberType type
) {
    public static CreateCastMemberCommand with(
            final String aName,
            final CastMemberType aType
    ) {
        return new CreateCastMemberCommand(aName, aType);
    }
}
