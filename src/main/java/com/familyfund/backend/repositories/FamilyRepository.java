package com.familyfund.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.familyfund.backend.modelo.Family;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
    //  Métodos personalizados si procede
}
