package com.fabricio.admin.catalogo.application.category.delete;

import com.fabricio.admin.catalogo.IntegrationTest;
import com.fabricio.admin.catalogo.domain.category.Category;
import com.fabricio.admin.catalogo.domain.category.CategoryGateway;
import com.fabricio.admin.catalogo.domain.category.CategoryID;
import com.fabricio.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fabricio.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAvalidId_whenCallsDeleteCategory_thenShouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
        verify(categoryGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAInvalidId_whenCallsDeleteCategory_thenShouldBeOk() {
        final var expectedId = CategoryID.from("123");

        Assertions.assertEquals(0, categoryRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(expectedId);
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAvalidId_whenGatewayThrowsException_thenShouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(expectedId);

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(expectedId);
        Assertions.assertEquals(1, categoryRepository.count());
    }
}
