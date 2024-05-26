package com.fabricio.admin.catalogo.application;

import com.fabricio.admin.catalogo.IntegrationTest;
import com.fabricio.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fabricio.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository repository;

    @Test
    void testInjects() {
        Assertions.assertNotNull(createCategoryUseCase);
        Assertions.assertNotNull(repository);
    }
}
