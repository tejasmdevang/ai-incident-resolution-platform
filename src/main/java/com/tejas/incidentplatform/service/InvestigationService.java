package com.tejas.incidentplatform.service;

import com.tejas.incidentplatform.dto.InvestigationResult;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentEvidence;
import com.tejas.incidentplatform.repository.IncidentEvidenceRepository;
import com.tejas.incidentplatform.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import com.tejas.incidentplatform.ai.IncidentAiClient;
import java.util.List;

@Service
public class InvestigationService {

    private final IncidentRepository incidentRepository;
    private final IncidentEvidenceRepository evidenceRepository;
    private final IncidentAiClient incidentAiClient;

    public InvestigationService(
            IncidentRepository incidentRepository,
            IncidentEvidenceRepository evidenceRepository,
            IncidentAiClient incidentAiClient
    ) {
        this.incidentRepository = incidentRepository;
        this.evidenceRepository = evidenceRepository;
        this.incidentAiClient = incidentAiClient;
    }

    public InvestigationResult analyze(Long incidentId) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Incident not found: " + incidentId
                        )
                );

        List<IncidentEvidence> evidence =
                evidenceRepository.findByIncidentIdOrderByObservedAtAsc(
                        incidentId
                );

        // LLM integration is the next step.
        StringBuilder context = new StringBuilder();
        context.append("Incident:\n");
        context.append("Title: ").append(incident.getTitle()).append("\n");
        context.append("Description: ").append(incident.getDescription()).append("\n");
        context.append("Service: ").append(incident.getServiceName()).append("\n");
        context.append("Severity: ").append(incident.getSeverity()).append("\n");
        context.append("Status: ").append(incident.getStatus()).append("\n\n");

        context.append("Evidence:\n");

        if (evidence.isEmpty()) {
            context.append("No operational evidence is available.\n");
        } else {
            for (IncidentEvidence item : evidence) {
                context.append("- Type: ").append(item.getType()).append("\n");
                context.append("  Source: ").append(item.getSource()).append("\n");
                context.append("  Observed At: ").append(item.getObservedAt()).append("\n");
                context.append("  Content: ").append(item.getContent()).append("\n");
    }
}

return incidentAiClient.investigate(context.toString());
        
        
    }
}