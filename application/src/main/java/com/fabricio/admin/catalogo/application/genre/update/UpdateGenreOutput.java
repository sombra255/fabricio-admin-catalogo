package com.fabricio.admin.catalogo.application.genre.update;

import com.fabricio.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(String id) {

    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }

    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }

}
