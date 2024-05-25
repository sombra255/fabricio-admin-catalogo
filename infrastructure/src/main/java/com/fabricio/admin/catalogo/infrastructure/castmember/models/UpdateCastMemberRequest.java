package com.fabricio.admin.catalogo.infrastructure.castmember.models;

import com.fabricio.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}
