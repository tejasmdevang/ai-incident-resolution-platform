package com.tejas.incidentplatform.ai.tools;

import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.repository.IncidentRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class IncidentTools {

    private final IncidentRepository incidentRepository;

    public IncidentTools(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Tool(description = """
        Update the status of an existing incident.
        Allowed statuses are OPEN, INVESTIGATING, MITIGATED, and RESOLVED.
        Use this tool only when the user explicitly requests a status change.
        """)
    public String updateIncidentStatus(Long incidentId, String status) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Incident not found: " + incidentId
                        )
                );

        IncidentStatus newStatus =
                IncidentStatus.valueOf(status.toUpperCase());

        incident.setStatus(newStatus);
        incidentRepository.save(incident);

        return "Incident " + incidentId
                + " status updated to " + newStatus;
    }
}