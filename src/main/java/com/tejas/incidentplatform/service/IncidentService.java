package com.tejas.incidentplatform.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.tejas.incidentplatform.dto.IncidentRequest;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.repository.IncidentRepository;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository){
        this.incidentRepository = incidentRepository;
    }

    public Incident createIncident(IncidentRequest request) {
        OffsetDateTime now = OffsetDateTime.now();

        Incident incident = new Incident();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setServiceName(request.getServiceName());
        incident.setSeverity(request.getSeverity());

        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedAt(now);
        incident.setUpdatedAt(now);

        return incidentRepository.save(incident);
    }

}
