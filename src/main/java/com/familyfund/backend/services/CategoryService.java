package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.dto.CategoryRequest;
import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.modelo.Category;

public interface CategoryService {
    Category save(Category category);
    Category createCategory(Long familyId, CategoryRequest request);
    Category findById(Long id);
    List<Category> findAll();
    List<Category> findByFamilyId(Long familyId);
    void deleteById(Long id);
    CategoryResponse toResponse(Category category);

}

