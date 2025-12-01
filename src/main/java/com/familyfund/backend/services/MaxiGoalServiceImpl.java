package com.familyfund.backend.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familyfund.backend.dto.MaxiGoalSavingResponse;
import com.familyfund.backend.dto.UpdateMaxiGoalRequest;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.MaxiGoalSaving;
import com.familyfund.backend.modelo.TransactionType;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.MaxiGoalRepository;
import com.familyfund.backend.repositories.MaxiGoalSavingRepository;
import com.familyfund.backend.repositories.UsuarioRepository;

@Service
public class MaxiGoalServiceImpl implements MaxiGoalService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    MaxiGoalRepository maxiGoalRepository;
    @Autowired
    MaxiGoalSavingRepository maxiGoalSavingRepository;
    @Autowired
    FamilyRepository familyRepository;
    @Autowired
    CategoryRepository categoryRepository;

    // AÑADIR SAVING POR USUARIO
    @Transactional
    public MaxiGoal addSaving(Long maxiGoalId, Double amount) {

        MaxiGoal maxiGoal = maxiGoalRepository.findById(maxiGoalId)
                .orElseThrow(() -> new RuntimeException("MaxiGoal not found"));

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Registrar aporte individual
        MaxiGoalSaving saving = new MaxiGoalSaving();
        saving.setAmount(amount);
        saving.setMaxiGoal(maxiGoal);
        saving.setUsuario(usuario);
        saving.setCreatedAt(LocalDate.now());
        maxiGoalSavingRepository.save(saving);

        // Recalcular total de ahorros del MaxiGoal
        double totalSave = maxiGoalSavingRepository.findByMaxiGoalId(maxiGoalId)
                .stream()
                .mapToDouble(MaxiGoalSaving::getAmount)
                .sum();
        maxiGoal.setActualSave(totalSave);

        // Comprobar si se ha alcanzado el objetivo
        if (maxiGoal.getActualSave() >= maxiGoal.getTargetAmount()) {
            maxiGoal.setAchieved(true);
            maxiGoalRepository.save(maxiGoal);

            MaxiGoal newGoal = new MaxiGoal();
            newGoal.setName("Nuevo objetivo");
            newGoal.setTargetAmount(1000.0);
            newGoal.setActualSave(0.0);
            newGoal.setAchieved(false);
            newGoal.setFamily(maxiGoal.getFamily());

            return maxiGoalRepository.save(newGoal);
        }

        return maxiGoalRepository.save(maxiGoal);
    }

    // AÑADIR SAVING AUTOMÁTICO (SISTEMA) (Negativo o positivo)
    @Transactional
    public MaxiGoalSaving createSystemSaving(Long maxiGoalId, double amount) {
        MaxiGoal maxiGoal = maxiGoalRepository.findById(maxiGoalId)
                .orElseThrow(() -> new RuntimeException("MaxiGoal no encontrado"));

        // Crear el saving de sistema
        MaxiGoalSaving saving = new MaxiGoalSaving();
        saving.setMaxiGoal(maxiGoal);
        saving.setAmount(amount);
        saving.setCreatedAt(LocalDate.now());
        saving.setSystem(true);
        saving.setUsuario(null);

        maxiGoalSavingRepository.save(saving);

        // Recalcular total de ahorros del MaxiGoal sumando todos los savings
        double totalSave = maxiGoalSavingRepository.findByMaxiGoalId(maxiGoalId)
                .stream()
                .mapToDouble(MaxiGoalSaving::getAmount)
                .sum();
        maxiGoal.setActualSave(totalSave);

        // Comprobar si se ha alcanzado el objetivo
        if (maxiGoal.getActualSave() >= maxiGoal.getTargetAmount()) {
            maxiGoal.setAchieved(true);
            maxiGoalRepository.save(maxiGoal);

            MaxiGoal newGoal = new MaxiGoal();
            newGoal.setName("Nuevo objetivo");
            newGoal.setTargetAmount(1000.0); // puedes parametrizar si quieres
            newGoal.setActualSave(0.0);
            newGoal.setAchieved(false);
            newGoal.setFamily(maxiGoal.getFamily());

            maxiGoalRepository.save(newGoal);
        }

        return saving;
    }

    // OBTENER TODOS LOS MAXIGOAL POR FAMILIA
    public List<MaxiGoal> getAllGoalsByFamily(Long familyId) {
        return maxiGoalRepository.findByFamilyId(familyId);
    }

    // OBTENER POR ID
    public MaxiGoal getGoal(Long id) {
        return maxiGoalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaxiGoal not found"));
    }

    // OBTENER TODOS LOS SAVINGS DE UN MAXIGOAL ID
    public List<MaxiGoalSaving> getAllSavings(Long id) {
        return maxiGoalSavingRepository.findByMaxiGoalId(id);
    }

    // OBTENER TODOS LOS SAVINGS DE UNA FAMILIA
    public List<MaxiGoalSaving> getAllSavingsbyFamily(Long familyId) {
        return maxiGoalSavingRepository.findAllByFamilyIdOrderByCreatedAtDesc(familyId);
    }

    // CONVERTIR A RESPONSE
    public MaxiGoalSavingResponse toResponse(MaxiGoalSaving saving) {

        Long usuarioId = null;
        String usuarioNombre = null;
        String photoUrl = null;

        if (!saving.isSystem() && saving.getUsuario() != null) {
            usuarioId = saving.getUsuario().getId();
            usuarioNombre = saving.getUsuario().getNombre();
            photoUrl = saving.getUsuario().getPhotoUrl();
        }

        return new MaxiGoalSavingResponse(
                saving.getId(),
                saving.getAmount(),
                saving.getCreatedAt(),
                usuarioId,
                usuarioNombre,
                photoUrl,
                saving.isSystem());
    }

    // CREAR NUEVO MAXIGOAL
    public MaxiGoal create(Long familyId, MaxiGoal maxiGoal) {

        // Bloqueamos que se cree un nuevo MaxiGoal si la familia ya tiene uno activo
        if (maxiGoalRepository.existsByFamilyIdAndAchievedFalse(familyId)) {
            throw new IllegalStateException("Ya existe un maxiGoal activo para esta familia");
        }

        // Asociamos el MaxiGoal a la familia
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        maxiGoal.setFamily(family);
        maxiGoal.setAchieved(false);
        maxiGoal.setTargetAmount(1000.0);
        maxiGoal.setActualSave(0.0);

        return maxiGoalRepository.save(maxiGoal);
    }

    // UPDATE MAXIGOAL
    public MaxiGoal updateMaxiGoal(Long id, UpdateMaxiGoalRequest dto) {
        MaxiGoal existing = maxiGoalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaxiGoal no encontrado"));

        if (dto.getName() != null)
            existing.setName(dto.getName());
        if (dto.getTargetAmount() != null)
            existing.setTargetAmount(dto.getTargetAmount());

        return maxiGoalRepository.save(existing);
    }

    // EVALUAR MAXIGOAL A FINAL DE MES
    @Transactional
    public void addRemainingToMaxiGoal() {
        LocalDate today = LocalDate.now();
        // Fecha "hoy" simulando el mes pasado
        // LocalDate today = LocalDate.now().minusMonths(1);
        YearMonth currentMonth = YearMonth.from(today);
        LocalDate lastDayOfMonth = currentMonth.atEndOfMonth();

        // Obtener todas las familias que tengan un MaxiGoal activo
        List<Long> familyIds = maxiGoalRepository.findAllActiveFamilyIds();

        for (Long familyId : familyIds) {
            // Obtener el MaxiGoal activo de la familia
            MaxiGoal maxiGoal = maxiGoalRepository.findByFamilyIdAndAchievedFalse(familyId)
                    .orElse(null);
            if (maxiGoal == null)
                continue;

            // Calcular ingresos y gastos del mes
            Double totalIngresos = categoryRepository.sumTransactionsByFamilyAndTypeAndMonth(
                    familyId, TransactionType.INCOME, currentMonth.getYear(), currentMonth.getMonthValue());
            Double totalGastos = categoryRepository.sumTransactionsByFamilyAndTypeAndMonth(
                    familyId, TransactionType.EXPENSE, currentMonth.getYear(), currentMonth.getMonthValue());

            // Evitar null
            totalIngresos = totalIngresos != null ? totalIngresos : 0.0;
            totalGastos = totalGastos != null ? totalGastos : 0.0;

            // Sumar solo los savings del mes actual
            Double ahorroActualMes = maxiGoal.getSavings() != null
                    ? maxiGoal.getSavings().stream()
                            .filter(s -> YearMonth.from(s.getCreatedAt()).equals(currentMonth))
                            .mapToDouble(MaxiGoalSaving::getAmount)
                            .sum()
                    : 0.0;

            // Calcular restante del mes
            double restante = totalIngresos - totalGastos - ahorroActualMes;

            if (restante > 0) {
                // Crear saving de sistema positivo con fecha último día del mes
                MaxiGoalSaving saving = new MaxiGoalSaving();
                saving.setMaxiGoal(maxiGoal);
                saving.setAmount(restante);
                saving.setSystem(true);
                saving.setCreatedAt(lastDayOfMonth);

                maxiGoalSavingRepository.save(saving);

                // Recalcular total de ahorros del MaxiGoal
                double totalSave = maxiGoalSavingRepository.findByMaxiGoalId(maxiGoal.getId())
                        .stream().mapToDouble(MaxiGoalSaving::getAmount).sum();
                maxiGoal.setActualSave(totalSave);

                // Comprobar si se ha alcanzado el objetivo
                if (maxiGoal.getActualSave() >= maxiGoal.getTargetAmount()) {
                    maxiGoal.setAchieved(true);
                    maxiGoalRepository.save(maxiGoal);

                    MaxiGoal newGoal = new MaxiGoal();
                    newGoal.setName("Nuevo objetivo");
                    newGoal.setTargetAmount(1000.0);
                    newGoal.setActualSave(0.0);
                    newGoal.setAchieved(false);
                    newGoal.setFamily(maxiGoal.getFamily());

                    maxiGoalRepository.save(newGoal);
                } else {
                    // Guardamos MaxiGoal actualizado con el nuevo actualSave
                    maxiGoalRepository.save(maxiGoal);
                }

                System.out.println("Saving de sistema añadido al MaxiGoal "
                        + maxiGoal.getName() + ": " + restante + "€ (mes " + currentMonth + ")");
            }
        }
    }

}
