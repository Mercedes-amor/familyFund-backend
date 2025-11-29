package com.familyfund.backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.familyfund.backend.services.GoalService;
import com.familyfund.backend.services.MaxiGoalService;

@Component
public class GoalScheduler {

    private final GoalService goalService;
    private final MaxiGoalService maxiGoalService;

    public GoalScheduler(GoalService goalService, MaxiGoalService maxiGoalService) {
        this.goalService = goalService;
        this.maxiGoalService = maxiGoalService;
    }

    // Se ejecutan el último día de cada mes a las 23:59
    // @Scheduled(cron = "*/10 * * * * ?") // Cada 10 segundos

    // Evaluar los goals
    @Scheduled(cron = "0 59 23 L * ?")
    public void autoMonthlyProcess() {
        //Primero evaluamos los mini objetivos que registrarán los gastos correspondientes si fueron cumplidos
        goalService.evaluateAllGoals();
        System.out.println("Evaluación de goals completada");

        //Luego con los fondos restantes se calcula el ahorro y se añade a MaxiGoal
        maxiGoalService.addRemainingToMaxiGoal();
        System.out.println("Ahorro de sistema añadido");
    }

}
