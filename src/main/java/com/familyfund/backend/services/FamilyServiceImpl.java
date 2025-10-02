package com.familyfund.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.FamilyRepository;

@Service
public class FamilyServiceImpl implements FamilyService {

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    CategoryService categoryService;

    // NUEVA FAMILIA
    @Override
    public Family createFamily(String name, Long userId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        Usuario usuario = usuarioService.findById(userId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        String generatedCode = UUID.randomUUID().toString().substring(0, 6);

        // Creamos la familia
        Family family = Family.builder()
                .name(name)
                .code(generatedCode)
                .usuarios(new ArrayList<>())
                .categories(new ArrayList<>())
                .build();

        // Añadimos el usuario y ligamos
        family.getUsuarios().add(usuario);
        usuario.setFamily(family);

        // Crear categoría INGRESOS automáticamente
        Category ingresosCategory = new Category();
        ingresosCategory.setName("INGRESOS");
        ingresosCategory.setFamily(family);
        family.getCategories().add(ingresosCategory);

        // Guardamos la familia; cascada se encargará de usuarios y categorías
        familyRepository.save(family);

        return family;
    }

    // Obtener DTO de Familia por Id
    @Override
    public FamilyResponse getFamilyById(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

        // Transformar categorías en CategoryResponse usando el método del
        // CategoryService
        List<CategoryResponse> categories = family.getCategories().stream()
                .map(categoryService::toResponse)
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
