package com.familyfund.backend.services;

import java.util.List;
import java.util.Optional;

import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.modelo.Family;

public interface FamilyService {
    FamilyResponse getFamilyById(Long familyId); //Obtener dto Family
    Family save(Family family);         // Crear o actualizar
    List<Family> findAll();             // Listar todas las familias 
    List<MemberResponse> getMembersByFamilyId(Long familyId); //Obtener dto miembros
    Family findById(Long id);           // Buscar por id
    void deleteById(Long id);           // Eliminar por id
    Optional<Family> findOptionalById(Long id);
}
