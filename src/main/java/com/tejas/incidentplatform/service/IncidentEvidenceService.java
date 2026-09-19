package com.tejas.incidentplatform.service;

import com.tejas.incidentplatform.dto.IncidentEvidenceRequest;
import com.tejas.incidentplatform.entity.IncidentEvidence;
import com.tejas.incidentplatform.repository.IncidentEvidenceRepository;
import com.tejas.incidentplatform.repository.IncidentRepository;
import org.springframework.stereotype.Service;

@Service
public class IncidentEvidenceService {

    private final IncidentEvidenceRepository evidenceRepository;
    private final IncidentRepository incidentRepository;

    public IncidentEvidenceService(
            IncidentEvidenceRepository evidenceRepository,
            IncidentRepository incidentRepository
    ) {
        this.evidenceRepository = evidenceRepository;
        this.incidentRepository = incidentRepository;
    }

    public IncidentEvidence addEvidence(
            Long incidentId,
            IncidentEvidenceRequest request
    ) {

        if (!incidentRepository.existsById(incidentId)) {
            throw new IllegalArgumentException("Incident not found: " + incidentId);
        }

        IncidentEvidence evidence = new IncidentEvidence(
                incidentId,
                request.type(),
                request.source(),
                request.content(),
                request.observedAt()
        );

        return evidenceRepository.save(evidence);
    }
}