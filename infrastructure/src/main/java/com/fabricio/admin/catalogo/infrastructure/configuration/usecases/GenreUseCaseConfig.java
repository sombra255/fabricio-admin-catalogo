package com.fabricio.admin.catalogo.infrastructure.configuration.usecases;

import com.fabricio.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.fabricio.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fabricio.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.fabricio.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fabricio.admin.catalogo.domain.category.CategoryGateway;
import com.fabricio.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }
}
