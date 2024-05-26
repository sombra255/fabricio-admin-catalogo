package com.fabricio.admin.catalogo.infrastructure.castmember.models;

import com.fabricio.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
