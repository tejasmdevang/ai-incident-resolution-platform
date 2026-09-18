package com.tejas.incidentplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record IncidentEvidenceRequest(

        @NotBlank
        String type,

        @NotBlank
        String source,

        @NotBlank
        String content,

        @NotNull
        OffsetDateTime observedAt

) {}