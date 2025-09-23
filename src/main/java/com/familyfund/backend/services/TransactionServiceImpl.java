package com.familyfund.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.TransactionRequest;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public TransactionServiceImpl(CategoryRepository categoryRepository,
            TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    // CREAR NUEVA TRANSACTION
    @Override
    public Transaction createTransaction(Long categoryId, TransactionRequest request, Usuario usuario) {
        // Instancia de categoría a través del categoryId enviado como parámetro
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));


        // Instancia de transaction
        Transaction transaction = new Transaction();
        transaction.setName(request.getName());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setAmount(request.getAmount());
        transaction.setUsuario(usuario); //Se recibe como parámetro
        transaction.setCategory(category);

        // El campo month se calculará automáticamente gracias al @PrePersist/@PreUpdate
        return transactionRepository.save(transaction);

    }

        // GUARDAR TRANSACTION
        public Transaction save(Transaction transaction){
           return transactionRepository.save(transaction);
        }
}
