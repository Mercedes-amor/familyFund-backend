package com.familyfund.backend.services;

import java.util.List;
import java.util.Optional;

import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.modelo.Family;

public interface FamilyService {
    Family createFamily(String name, Long userId); //Crear familia
    FamilyResponse getFamilyById(Long familyId); //Obtener dto Family por id
    Family findByCode(String code); //Obtener dto Family por code
    Family save(Family family);         // Crear o actualizar
    List<Family> findAll();             // Listar todas las familias 
    FamilyResponse toFamilyResponse(Family family); //Convertir a dto
    List<MemberResponse> getMembersByFamilyId(Long familyId); //Obtener dto miembros
    Family findById(Long id);           // Buscar por id
    void deleteById(Long id);           // Eliminar por id
    public Optional<Family> findOptionalById(Long id);
}
