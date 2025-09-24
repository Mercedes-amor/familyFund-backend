package com.familyfund.backend.services;

import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.repositories.FamilyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;

    // @Autowired
    public FamilyServiceImpl(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    // Obtener DTO de Familia por Id
    public FamilyResponse getFamilyById(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

        List<CategoryResponse> categories = family.getCategories().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();

        return new FamilyResponse(family.getId(), family.getName(), categories);
    }

    // Crear y guardar Familia
    @Override
    public Family save(Family family) {
        return familyRepository.save(family);
    }

    // Mostrar todas las familias
    @Override
    public List<Family> findAll() {
        return familyRepository.findAll();
    }

    // Obtener DTO miembros familia
    public List<MemberResponse> getMembersByFamilyId(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

        return family.getUsuarios().stream()
                .map(u -> new MemberResponse(u.getId(), u.getNombre(), u.getEmail()))
                .toList();
    }

    // Encontrar por Id
    @Override
    public Family findById(Long id) {
        Optional<Family> family = familyRepository.findById(id);
        return family.orElse(null); // orElse??
    }

    // Borrar por id
    @Override
    public void deleteById(Long id) {
        familyRepository.deleteById(id);
    }

    // Método que devuelve la familia como Optional
    public Optional<Family> findOptionalById(Long id) {
        return familyRepository.findById(id);
    }
}
