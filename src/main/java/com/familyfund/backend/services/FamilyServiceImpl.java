package com.familyfund.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.MaxiGoalResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.MaxiGoalRepository;
import com.familyfund.backend.repositories.UsuarioRepository;

@Service
public class FamilyServiceImpl implements FamilyService {

    @Autowired
    FamilyRepository familyRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MaxiGoalRepository maxiGoalRepository;

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

        // Código aleatorio para unirse a familia
        String generatedCode = UUID.randomUUID().toString().substring(0, 6);

        // Crear familia
        Family family = Family.builder()
                .name(name)
                .code(generatedCode)
                .usuarios(new ArrayList<>())
                .categories(new ArrayList<>())
                .build();

        // Relación usuario ↔ familia
        family.getUsuarios().add(usuario);
        usuario.setFamily(family);

        // Crear categoría INGRESOS automáticamente
        Category ingresosCategory = Category.builder()
                .name("INGRESOS")
                .family(family)
                .transactions(new ArrayList<>()) // EVitamos el null
                .build();

        family.getCategories().add(ingresosCategory);

        // Guardar la familia para obtener ID
        Family savedFamily = familyRepository.save(family);

        // Crear MaxiGoal automáticamente con el familyId
        MaxiGoal maxigoal = new MaxiGoal();
        maxigoal.setName("MaxiGoal inicial");
        maxigoal.setTargetAmount(1000.0);
        maxigoal.setActualSave(0.0);
        maxigoal.setAchieved(false);
        maxigoal.setFamily(savedFamily);

        maxiGoalRepository.save(maxigoal);

        return savedFamily;
    }

    // Obtener DTO de Familia por Id
    @Override
    public FamilyResponse getFamilyById(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

        // Transformar categorías en CategoryResponse usando CategoryService
        List<CategoryResponse> categories = family.getCategories().stream()
                .map(categoryService::toResponse)
                .toList();

        // Buscar el MaxiGoal activo
        MaxiGoalResponse maxiGoalResponse = maxiGoalRepository
                .findByFamilyIdAndAchievedFalse(familyId)
                .map(g -> new MaxiGoalResponse(
                        g.getId(),
                        g.getName(),
                        g.getTargetAmount(),
                        g.getActualSave(),
                        g.getAchieved()))
                .orElse(null);

        return new FamilyResponse(
                family.getId(),
                family.getName(),
                categories,
                maxiGoalResponse);
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
                .map(u -> new MemberResponse(u.getId(), u.getNombre(), u.getEmail(), u.getPhotoUrl()))
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
    @Transactional // asegura que la operación se ejecute dentro de la transacción.
    public void deleteById(Long id) {
        // 1. Desvinculamos usuarios
        usuarioRepository.findByFamily_Id(id).forEach(user -> user.setFamily(null));

        // 2. Guardamos los usuarios actualizados
        usuarioRepository.flush(); // asegura que se aplique antes del delete de la familia

        // 3. Borramos las categorías asociadas
        categoryRepository.deleteByFamilyId(id);

        // 4. Borramos la familia
        familyRepository.deleteById(id);
    }

    // Método que devuelve la familia como Optional
    public Optional<Family> findOptionalById(Long id) {
        return familyRepository.findById(id);
    }
}
