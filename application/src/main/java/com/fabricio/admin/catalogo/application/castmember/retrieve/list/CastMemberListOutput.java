package com.fabricio.admin.catalogo.application.castmember.retrieve.list;

import com.fabricio.admin.catalogo.domain.castmember.CastMember;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {

    public static CastMemberListOutput from(CastMember aCastMember) {
        return new CastMemberListOutput(
                aCastMember.getId().getValue(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt(),
                aCastMember.getUpdatedAt()
        );
    }

}
