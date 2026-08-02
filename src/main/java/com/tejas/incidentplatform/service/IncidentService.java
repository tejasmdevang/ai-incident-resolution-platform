package com.tejas.incidentplatform.service;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import com.tejas.incidentplatform.dto.IncidentRequest;
import com.tejas.incidentplatform.dto.IncidentResponse;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.repository.IncidentRepository;

import java.time.OffsetDateTime;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository){
        this.incidentRepository = incidentRepository;
    }

    public IncidentResponse createIncident(IncidentRequest request) {
        OffsetDateTime now = OffsetDateTime.now();

        Incident incident = new Incident();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setServiceName(request.getServiceName());
        incident.setSeverity(request.getSeverity());

        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedAt(now);
        incident.setUpdatedAt(now);

        Incident savedIncident = incidentRepository.save(incident);

        return mapToResponse(savedIncident);
    }

    private IncidentResponse mapToResponse(Incident incident) {
        IncidentResponse response = new IncidentResponse();

        response.setId(incident.getId());
        response.setTitle(incident.getTitle());
        response.setDescription(incident.getDescription());
        response.setServiceName(incident.getServiceName());
        response.setSeverity(incident.getSeverity());
        response.setStatus(incident.getStatus());
        response.setCreatedAt(incident.getCreatedAt());
        response.setUpdatedAt(incident.getUpdatedAt());

        return response;
    }
}
