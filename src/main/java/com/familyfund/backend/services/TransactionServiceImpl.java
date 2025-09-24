package com.familyfund.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.TransactionRequest;
import com.familyfund.backend.dto.TransactionResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.TransactionRepository;
import com.familyfund.backend.repositories.UsuarioRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    // CREAR NUEVA TRANSACTION
    @Override
public TransactionResponse createTransaction(Long categoryId, TransactionRequest request) {
    // Recuperar categoría
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

    // Obtener usuario autenticado
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName(); 
    Usuario usuario = usuarioRepository.findByEmail(username);
    if (usuario == null) {
        throw new RuntimeException("Usuario no encontrado");
    }

    // Crear transaction
    Transaction transaction = new Transaction();
    transaction.setName(request.getName());
    transaction.setType(request.getType());
    transaction.setDate(request.getDate());
    transaction.setAmount(request.getAmount());
    transaction.setUsuario(usuario);
    transaction.setCategory(category);

    transaction = transactionRepository.save(transaction);

    // Transformar a DTO
    return new TransactionResponse(
        transaction.getId(),
        transaction.getName(),
        transaction.getType(),
        transaction.getDate(),
        transaction.getAmount(),
        category.getId()
    );
}


    // GUARDAR TRANSACTION
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
