package com.familyfund.backend.services;

import com.familyfund.backend.dto.TransactionRequest;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.modelo.Usuario;

public interface TransactionService {
    Transaction createTransaction(Long categoryId, TransactionRequest request, Usuario usuario);
    Transaction save(Transaction transaction);
}

