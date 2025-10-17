package com.familyfund.backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.modelo.Usuario;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Obtener las transacciones del mes actual
    @Query("""
                SELECT t
                FROM Transaction t
                WHERE t.category.family.id = :familyId
                  AND YEAR(t.date) = :year
                  AND MONTH(t.date) = :month
            """)
    List<Transaction> findByFamilyAndMonth(
            @Param("familyId") Long familyId,
            @Param("year") int year,
            @Param("month") int month);

    // Obtener las transacciones de un familia
    @Query("""
                SELECT t
                FROM Transaction t
                WHERE t.category.family.id = :familyId
            """)
    List<Transaction> findByFamily(@Param("familyId") Long familyId);

    // Obtener transacciones de una categoría
    List<Transaction> findByCategoryId(Long categoryId);

    // Borrar todas las transacciones al borrar su categoría
    void deleteAllByCategoryId(Long categoryId);

    // Borrar las transacciones del mes actual. Filtramos directamente por la fecha
    // ya que los atributos month y year nos daban error por inconsistencia
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.category.id = :categoryId AND t.date >= :start AND t.date < :end")
    void deleteByCategoryIdAndDateBetween(@Param("categoryId") Long categoryId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    // Al borrar un usuario buscamos todas sus transacciones y las volvemos a NULL
    @Modifying
    @Query("UPDATE Transaction ut SET ut.usuario = null WHERE ut.usuario = :usuario")
    void setUsuarioNull(@Param("usuario") Usuario usuario);

}
