package com.fabricio.admin.catalogo.application.castmember.create;

import com.fabricio.admin.catalogo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
