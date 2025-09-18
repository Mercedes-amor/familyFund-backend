package com.familyfund.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
