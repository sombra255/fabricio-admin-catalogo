package com.fabricio.admin.catalogo.application.category.delete;

import com.fabricio.admin.catalogo.domain.category.CategoryGateway;
import com.fabricio.admin.catalogo.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(final String anIn) {
        final var anId = CategoryID.from(anIn);
        this.categoryGateway.deleteById(anId);
    }
}
