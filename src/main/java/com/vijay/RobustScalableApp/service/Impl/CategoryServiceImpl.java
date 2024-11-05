package com.vijay.RobustScalableApp.service.Impl;

import com.vijay.RobustScalableApp.entity.Category;
import com.vijay.RobustScalableApp.repository.CategoryRepository;
import com.vijay.RobustScalableApp.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
@Log4j2
@EnableAsync
public class CategoryServiceImpl extends GenericAsyncService<Category, Long> implements CategoryService {
    public CategoryServiceImpl(CategoryRepository categoryRepository, Executor executor) {
        super(categoryRepository, Category.class, executor);

    }
    @Override
    public String findUserName() {
        return "Welcome To spring boot";
    }
}
