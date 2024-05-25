package com.fabricio.admin.catalogo.domain.genre;

import com.fabricio.admin.catalogo.domain.AggregateRoot;
import com.fabricio.admin.catalogo.domain.category.CategoryID;
import com.fabricio.admin.catalogo.domain.exceptions.NotificationException;
import com.fabricio.admin.catalogo.domain.utils.InstantUtils;
import com.fabricio.admin.catalogo.domain.validation.ValidationHandler;
import com.fabricio.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Genre extends AggregateRoot<GenreID> implements Cloneable {

    private final Instant createdAt;
    private String name;
    private List<CategoryID> categories;
    private boolean active;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeleatedAt
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.deletedAt = aDeleatedAt;

        selfValidate();
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeleatedAt
    ) {
        return new Genre(
                anId,
                aName,
                isActive,
                categories,
                aCreatedAt,
                anUpdatedAt,
                aDeleatedAt
        );
    }

    public static Genre with(
            final Genre aGenre
    ) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(handler, this).validate();
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }

    public Genre addCategory(CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }
        if (Objects.isNull(this.categories)) {
            this.categories = new ArrayList<>();
        }
        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }
        if (Objects.nonNull(this.categories)) {
            this.categories.remove(aCategoryID);
            this.updatedAt = InstantUtils.now();
        }
        return this;
    }

    public Genre addCategories(List<CategoryID> aCategoriesID) {
        if (aCategoriesID == null || aCategoriesID.isEmpty()) {
            return this;
        }
        if (Objects.isNull(this.categories)) {
            this.categories = new ArrayList<>();
        }
        this.categories.addAll(aCategoriesID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Genre clone() {
        try {
            Genre genre = (Genre) super.clone();
            return genre;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
