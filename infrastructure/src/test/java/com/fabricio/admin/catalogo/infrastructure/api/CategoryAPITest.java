package com.fabricio.admin.catalogo.infrastructure.api;

import com.fabricio.admin.catalogo.ControllerTest;
import com.fabricio.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fabricio.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fabricio.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fabricio.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.fabricio.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fabricio.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.fabricio.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.fabricio.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.fabricio.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fabricio.admin.catalogo.domain.category.Category;
import com.fabricio.admin.catalogo.domain.category.CategoryID;
import com.fabricio.admin.catalogo.domain.exceptions.DomainException;
import com.fabricio.admin.catalogo.domain.exceptions.NotFoundException;
import com.fabricio.admin.catalogo.domain.pagination.Pagination;
import com.fabricio.admin.catalogo.domain.validation.Error;
import com.fabricio.admin.catalogo.domain.validation.handler.Notification;
import com.fabricio.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fabricio.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;
    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAvalidId_whenCallsGetCategory_thenShouldReturnCategory() throws Exception {
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        //when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(expectedId)));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(expectedName)));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(expectedDescription)));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.is_active", equalTo(expectedIsActive)));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_thenShouldReturnNotFound() throws Exception {
        //given
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        //when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = "123";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

        final var anInput =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception {
        //given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var anInput =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        final var anInput =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
        response.andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(expectedErrorCount)));
        response.andExpect(MockMvcResultMatchers.jsonPath("errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAvalidId_whenCallsDeleteCategory_thenShouldBeReturnNoContent() throws Exception {
        //given
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doNothing().when(deleteCategoryUseCase).execute(any());

        //when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(expectedId.getValue());
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
