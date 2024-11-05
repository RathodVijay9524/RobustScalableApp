package com.vijay.RobustScalableApp.repository;


import com.vijay.RobustScalableApp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
