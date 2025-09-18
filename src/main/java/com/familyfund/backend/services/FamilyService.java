package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.modelo.Family;

public interface FamilyService {
    Family save(Family family);         // Crear o actualizar
    List<Family> findAll();             // Listar todas las familias
    Family findById(Long id);           // Buscar por id
    void deleteById(Long id);           // Eliminar por id
}
