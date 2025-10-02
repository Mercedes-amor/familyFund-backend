package com.familyfund.backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.familyfund.backend.services.GoalService;

@Component
public class GoalScheduler {

    private final GoalService goalService;

    public GoalScheduler(GoalService goalService) {
        this.goalService = goalService;
    }

    // Se ejecuta el último día de cada mes a las 23:59
    @Scheduled(cron = "0 59 23 L * ?")
    // @Scheduled(cron = "0 * * * * ?") //Cada minuto
    // @Scheduled(cron = "*/10 * * * * ?") // Cada 10 segundos
    public void autoEvaluateGoals() {
        goalService.evaluateAllGoals();
        System.out.println("Evaluación de goals completada (incluyendo meses pasados)");
    }
}
