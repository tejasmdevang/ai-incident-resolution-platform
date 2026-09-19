package com.tejas.incidentplatform.service;

import com.tejas.incidentplatform.dto.InvestigationResult;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentEvidence;
import com.tejas.incidentplatform.repository.IncidentEvidenceRepository;
import com.tejas.incidentplatform.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import com.tejas.incidentplatform.ai.IncidentAiClient;
import java.util.List;
import com.tejas.incidentplatform.rag.RunbookSearchService;
import org.springframework.ai.document.Document;

@Service
public class InvestigationService {

    private final IncidentRepository incidentRepository;
    private final IncidentEvidenceRepository evidenceRepository;
    private final IncidentAiClient incidentAiClient;
    private final RunbookSearchService runbookSearchService;

    public InvestigationService(
            IncidentRepository incidentRepository,
            IncidentEvidenceRepository evidenceRepository,
            IncidentAiClient incidentAiClient,
            RunbookSearchService runbookSearchService
    ) {
        this.incidentRepository = incidentRepository;
        this.evidenceRepository = evidenceRepository;
        this.incidentAiClient = incidentAiClient;
        this.runbookSearchService = runbookSearchService;
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
    
        String searchQuery = incident.getTitle() + " "
                + incident.getDescription() + " "
                + evidence.stream()
                        .map(IncidentEvidence::getContent)
                        .reduce("", (a, b) -> a + " " + b);
    
        List<Document> relevantRunbooks =
                runbookSearchService.search(searchQuery);
    
        context.append("\nRelevant Runbooks:\n");
    
        if (relevantRunbooks.isEmpty()) {
            context.append("No relevant runbooks found.\n");
        } else {
            for (Document document : relevantRunbooks) {
                context.append(document.getText())
                        .append("\n\n");
            }
        }
    
        return incidentAiClient.investigate(context.toString());
    }
}