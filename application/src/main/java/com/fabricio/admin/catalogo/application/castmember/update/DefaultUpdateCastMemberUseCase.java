package com.fabricio.admin.catalogo.application.castmember.update;

import com.fabricio.admin.catalogo.domain.Identifier;
import com.fabricio.admin.catalogo.domain.castmember.CastMember;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberID;
import com.fabricio.admin.catalogo.domain.exceptions.DomainException;
import com.fabricio.admin.catalogo.domain.exceptions.NotFoundException;
import com.fabricio.admin.catalogo.domain.exceptions.NotificationException;
import com.fabricio.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    private static Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }

    private static void notify(final Identifier anId, final Notification notification) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId.getValue()), notification);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final var anId = CastMemberID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var aMember = castMemberGateway.findById(anId).orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.validate(() -> aMember.update(aName, aType));

        if (notification.hasError()) {
            notify(anId, notification);
        }

        return UpdateCastMemberOutput.from(castMemberGateway.update(aMember));
    }
}
