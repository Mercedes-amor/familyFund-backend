package com.familyfund.backend.services;

import java.util.List;

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
import com.familyfund.backend.security.UserDetailsImpl;

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
                category.getId(),
                transaction.getUsuario());
    }

    // GUARDAR TRANSACTION
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // OBTENER LISTADO POR FAMILY ID
    @Override
    public List<Transaction> findByFamilyId(Long familyId) {
        return transactionRepository.findByFamily(familyId);
    }

    // OBTENER TRANSACCIONES POR CATEGORIA ID
    public List<Transaction> findByCategoryId(Long categoryId) {
        return transactionRepository.findByCategoryId(categoryId);
    }

    // ACTUALIZAR TRANSACTION SOLO POR EL CREADOR
    public TransactionResponse updateTransaction(Long transactionId, TransactionRequest request) {
        // Obtenemos el usuario logueado
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Transaction transactionToEdit = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verificamos que el usuario actual es el dueño
        if (!transactionToEdit.getUsuario().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para editar esta transacción");
        }

        // Actualizamos solo nombre e importe
        transactionToEdit.setName(request.getName());
        transactionToEdit.setAmount(request.getAmount());

        Transaction saved = transactionRepository.save(transactionToEdit);

        return new TransactionResponse(
                saved.getId(),
                saved.getName(),
                saved.getType(),
                saved.getDate(),
                saved.getAmount(),
                saved.getCategory().getId(),
                saved.getUsuario());
    }


    // ELIMINAR TRANSACTION
    public void deleteTransaction(Long transactionId) {
        // Obtenemos el usuario logueado
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        //Obtenemos transaction a borrar        
        Transaction transactionToDelete = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verificamos que el usuario actual es el dueño
        if (!transactionToDelete.getUsuario().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para borrar esta transacción");
        }
        if (!transactionRepository.existsById(transactionId)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(transactionId);
    }
}
