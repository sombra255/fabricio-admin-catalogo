package com.fabricio.admin.catalogo.application.castmember.update;

import com.fabricio.admin.catalogo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
