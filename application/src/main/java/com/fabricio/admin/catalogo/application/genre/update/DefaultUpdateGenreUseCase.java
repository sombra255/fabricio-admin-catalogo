package com.fabricio.admin.catalogo.application.genre.update;

import com.fabricio.admin.catalogo.domain.Identifier;
import com.fabricio.admin.catalogo.domain.category.CategoryGateway;
import com.fabricio.admin.catalogo.domain.category.CategoryID;
import com.fabricio.admin.catalogo.domain.exceptions.DomainException;
import com.fabricio.admin.catalogo.domain.exceptions.NotFoundException;
import com.fabricio.admin.catalogo.domain.exceptions.NotificationException;
import com.fabricio.admin.catalogo.domain.genre.Genre;
import com.fabricio.admin.catalogo.domain.genre.GenreGateway;
import com.fabricio.admin.catalogo.domain.genre.GenreID;
import com.fabricio.admin.catalogo.domain.validation.Error;
import com.fabricio.admin.catalogo.domain.validation.ValidationHandler;
import com.fabricio.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    private static Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.from(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var aCategories = toCategoryID(aCommand.categories());

        final var aGenre = genreGateway.findById(anId).orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(aCategories));
        notification.validate(() -> aGenre.update(aName, isActive, aCategories));

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(aCommand.id()), notification
            );
        }

        return UpdateGenreOutput.from(genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private List<CategoryID> toCategoryID(final List<String> aCategories) {
        return aCategories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
