package service;

public record OccasionShortfallResult(
        int goalId,
        String goalName,
        double targetAmount,
        double savedAmount,
        double shortfall,
        boolean needsFundingDecision,
        String message
) {}