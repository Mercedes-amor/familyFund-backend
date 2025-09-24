package com.familyfund.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.familyfund.backend.modelo.Transaction;

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

        
        //Obtener las transacciones de un familia
        @Query("""
                            SELECT t
                            FROM Transaction t
                            WHERE t.category.family.id = :familyId
                        """)
        List<Transaction> findByFamily(@Param("familyId") Long familyId);

}
