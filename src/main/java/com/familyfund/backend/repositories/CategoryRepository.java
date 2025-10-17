package com.familyfund.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.familyfund.backend.modelo.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Obtener todas las categorías de una Familia
    // el guion bajo _ entre Family y Id le indica a Spring Data que debe navegar la
    // relación.
    List<Category> findByFamily_Id(Long familyId);

    // Devuelve solo categorías NO BORRADAS
    List<Category> findByFamily_IdAndDeletedFalse(Long familyId);

    // Histórico categorías
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.transactions t WHERE c.family.id = :familyId")
    List<Category> findAllIncludingDeletedWithTransactions(@Param("familyId") Long familyId);

    @Transactional // asegura que la operación se ejecute dentro de la transacción.
    @Modifying
    @Query("DELETE FROM Category c WHERE c.family.id = :familyId")
    void deleteByFamilyId(@Param("familyId") Long familyId);

}
