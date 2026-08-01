package com.tejas.incidentplatform.dto;

import com.tejas.incidentplatform.entity.Severity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class IncidentRequest {

    @NotNull
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String serviceName;

    @NotNull
    private Severity severity;

}
