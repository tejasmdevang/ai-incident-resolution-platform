package com.tejas.incidentplatform.dto;

import com.tejas.incidentplatform.entity.Severity;
import java.time.OffsetDateTime;
import com.tejas.incidentplatform.entity.IncidentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncidentResponse {

    private Long id;

    private String title;

    private String description;

    private String serviceName;

    private Severity severity;

    private IncidentStatus status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
