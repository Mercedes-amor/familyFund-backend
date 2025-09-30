package com.familyfund.backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.CategoryRequest;
import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.TransactionRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // @Autowired
    // private TransactionService transactionService;

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
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada"));

        Category category = new Category();
        category.setName(request.getName());
        category.setLimit(request.getLimit());
        category.setFamily(family);

        return categoryRepository.save(category);
    }

    // CREAR DTO CategoryResponse
    public CategoryResponse toResponse(Category category) {
        double totalSpent = category.getTransactions().stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        Double limit = category.getLimit();
        Double remaining = (limit != null) ? (limit - totalSpent) : null;
        Double percentage = (limit != null && limit > 0)
                ? (totalSpent / limit) * 100
                : null;

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setLimit(limit);
        response.setTotalSpent(totalSpent);
        response.setRemaining(remaining);
        response.setPercentage(percentage);

        return response;
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

    // TOTAL GASTOS DE UNA CATEGORÍA Y MES (YYYY-MM)
    public double getTotalSpentInMonth(Long categoryId, String yearMonth) {
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        return transactionRepository.findByCategoryId(categoryId)
                .stream()
                .filter(t -> {
                    LocalDate date = t.getDate();
                    return date != null && date.getYear() == year && date.getMonthValue() == month;
                })
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

}
