package service;

import java.util.List;

public record SavingsSuggestion(
        double leftoverAmount,
        List<String> options,
        String message
) {}