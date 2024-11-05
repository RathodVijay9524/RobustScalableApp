package com.vijay.RobustScalableApp.service;

import com.vijay.RobustScalableApp.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryService extends AsyncCrudOperations<Category, Long> {
    public String findUserName();
}
