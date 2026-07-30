package service;

import java.util.List;

public record IncomeAllocationResult(
        double amount,
        List<String> options,
        String message
) {}