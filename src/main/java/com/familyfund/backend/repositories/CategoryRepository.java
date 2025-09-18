package com.familyfund.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
