package com.fabricio.admin.catalogo.application.category.delete;

import com.fabricio.admin.catalogo.application.UseCaseTest;
import com.fabricio.admin.catalogo.domain.category.Category;
import com.fabricio.admin.catalogo.domain.category.CategoryGateway;
import com.fabricio.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    // 1. Teste do caminho feliz - recebo um ID e faço a deleção
    // 2. Teste passando um ID inválido
    // 3. Teste caso ocorra um erro generico vindo do gateway

    @Test
    void givenAvalidId_whenCallsDeleteCategory_thenShouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(expectedId);

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAInvalidId_whenCallsDeleteCategory_thenShouldBeOk() {
        final var expectedId = CategoryID.from("123");

        doNothing().when(categoryGateway).deleteById(expectedId);

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAvalidId_whenGatewayThrowsException_thenShouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(expectedId);

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(expectedId);
    }
}
