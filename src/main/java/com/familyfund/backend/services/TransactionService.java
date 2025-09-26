package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.dto.TransactionRequest;
import com.familyfund.backend.dto.TransactionResponse;
import com.familyfund.backend.modelo.Transaction;

public interface TransactionService {
    Transaction save(Transaction transaction);

    TransactionResponse createTransaction(Long categoryId, TransactionRequest request);

    List<Transaction> findByFamilyId(Long familyId);

    List<Transaction> findByCategoryId(Long categoryId);

    TransactionResponse updateTransaction(Long transactionId, TransactionRequest request);

    void deleteTransaction(Long transactionId);
}
