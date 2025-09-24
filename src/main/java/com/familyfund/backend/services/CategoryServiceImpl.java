package com.familyfund.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.CategoryRequest;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FamilyService familyService;

    // Usamos @override para
    // Asegurarnos de que realmente estamos implementando la interfaz correcta
    // Evitar errores de escritura en la firma del método.
    // Mejorar la legibilidad del código

    // GUARDAR CATEGORY
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // CREAR CATEGORÍA A PARTIR DTO
    public Category createCategory(Long familyId, CategoryRequest request) {
        Family family = familyService.findById(familyId);
        if (family == null) {
            throw new IllegalArgumentException("Familia no encontrada");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setFamily(family);

        return categoryRepository.save(category);
    }

    // OBTENER POR ID
    @Override
    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    // OBTENER TODAS
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // OBTENER LISTADO POR FAMILIA ID
    @Override
    public List<Category> findByFamilyId(Long familyId) {
        return categoryRepository.findByFamily_Id(familyId);
    }

    // BORRAR POR ID
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
