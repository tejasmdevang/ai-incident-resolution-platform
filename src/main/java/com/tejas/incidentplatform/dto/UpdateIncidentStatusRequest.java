package com.tejas.incidentplatform.dto;

import com.tejas.incidentplatform.entity.IncidentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateIncidentStatusRequest {

    @NotNull
    private IncidentStatus status;

}