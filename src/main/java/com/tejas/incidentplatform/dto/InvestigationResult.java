package com.tejas.incidentplatform.dto;

import java.util.List;

public record InvestigationResult(
        String summary,
        String probableRootCause,
        double confidence,
        List<String> evidenceUsed,
        List<String> recommendedActions
) {
}