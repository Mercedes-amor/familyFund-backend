package com.familyfund.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    //Obtener todas las categorías de una Familia    
    List<Category> findByFamilyId(Long familyId);

}
