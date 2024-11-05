package com.vijay.RobustScalableApp.controller;

import com.vijay.RobustScalableApp.entity.Category;
import com.vijay.RobustScalableApp.model.CategoryRequest;
import com.vijay.RobustScalableApp.model.CategoryResponse;
import com.vijay.RobustScalableApp.service.CategoryService;
import com.vijay.RobustScalableApp.service.Impl.CategoryServiceImpl;
import com.vijay.RobustScalableApp.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/message")
    public String findUserName() {
        String message = categoryService.findUserName();
        return message;
    }
    @PostMapping
    public CompletableFuture<ResponseEntity<CategoryResponse>> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = Mapper.toEntity(categoryRequest, Category.class);
        return categoryService.create(category)
                .thenApply(savedCategory -> ResponseEntity.ok(Mapper.toDto(savedCategory, CategoryResponse.class)));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id)
                .thenApply(category -> ResponseEntity.ok(Mapper.toDto(category, CategoryResponse.class)));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CategoryResponse>>> getAllCategories() {
        return categoryService.getAll()
                .thenApply(categories -> categories.stream()
                        .map(category -> Mapper.toDto(category, CategoryResponse.class))
                        .collect(Collectors.toList()))
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<CategoryResponse>> updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Long id) {
        Category category = Mapper.toEntity(categoryRequest, Category.class);
        category.setId(id);
        return categoryService.update(category, id)
                .thenApply(updatedCategory -> ResponseEntity.ok(Mapper.toDto(updatedCategory, CategoryResponse.class)));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteCategory(@PathVariable Long id) {
        return categoryService.delete(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }



}
