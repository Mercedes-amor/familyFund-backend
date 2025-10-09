package com.familyfund.backend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familyfund.backend.dto.CategoryRequest;
import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.TransactionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager em;

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
        category.setTransactions(new ArrayList<>());

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
        response.setDeleted(category.isDeleted());

        return response;
    }

    // <---- MÉTODOS QUE OBVIAN LAS CATEGORÍAS DE deleted = true ---->//
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

    // <---- MÉTODO INCLUYE LAS CATEGORÍAS DE deleted = true ---->//
    @Transactional
    public List<Category> getCategoriesHistory(Long familyId) {
        // Desactivar temporalmente el filtro
        Session session = em.unwrap(Session.class);
        session.disableFilter("deletedFilter");

        List<Category> categories = categoryRepository.findAllIncludingDeletedWithTransactions(familyId);

        // Opcional: volver a activar el filtro después
        session.enableFilter("deletedFilter");

        return categories;
    }

    // SOFT-DELETE - BORRAR POR ID
    @Override
    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        categoryRepository.delete(category); // @SQLDelete se encargará de hacer UPDATE deleted = true en vez de DELETE
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate inicioSiguienteMes = inicioMes.plusMonths(1);

        transactionRepository.deleteByCategoryIdAndDateBetween(categoryId, inicioMes, inicioSiguienteMes);

        // Soft delete de la categoría
        categoryRepository.delete(category);
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
