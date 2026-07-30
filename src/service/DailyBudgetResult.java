package service;

import java.time.LocalDate;

public record DailyBudgetResult(
        LocalDate date,
        double expectedDailyBudget,
        double actualSpentToday,
        double difference,
        double recalculatedDailyBudget,
        boolean needsAllocationDecision,
        String message
) {}