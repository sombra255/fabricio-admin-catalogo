package com.fabricio.admin.catalogo.application.category.retrieve.list;

import com.fabricio.admin.catalogo.application.UseCaseTest;
import com.fabricio.admin.catalogo.domain.category.Category;
import com.fabricio.admin.catalogo.domain.category.CategoryGateway;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ListCategoriesUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        var category1 = Category.newCategory("Filmes", "Filme mais assistido", true);
        var category2 = Category.newCategory("Series", "Serie mais assistida", true);
        var categories = List.of(category1, category2);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 2;
//        final var expectedResult = expectedPagination.map(CategoryListOutput::from);
        List<CategoryListOutput> categoryListOutputs = expectedPagination.items().stream().map(CategoryListOutput::from).toList();
        final var expectedResult = new Pagination<>(expectedPagination.currentPage(), expectedPagination.perPage(), categoryListOutputs.size(), categoryListOutputs);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        var categories = List.<Category>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 0;
        List<CategoryListOutput> categoryListOutputs = expectedPagination.items().stream().map(CategoryListOutput::from).toList();
        final var expectedResult = new Pagination<>(expectedPagination.currentPage(), expectedPagination.perPage(), categoryListOutputs.size(), categoryListOutputs);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(categoryGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

//        Assertions.assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> useCase.execute(aQuery))
//                .withMessage(expectedErrorMessage);
    }
}
