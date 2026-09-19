package com.tejas.incidentplatform.repository;

import com.tejas.incidentplatform.entity.IncidentEvidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentEvidenceRepository
        extends JpaRepository<IncidentEvidence, Long> {

    List<IncidentEvidence> findByIncidentIdOrderByObservedAtAsc(Long incidentId);
}