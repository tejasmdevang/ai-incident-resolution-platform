package com.tejas.incidentplatform.service;

import java.time.OffsetDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tejas.incidentplatform.dto.IncidentRequest;
import com.tejas.incidentplatform.dto.IncidentResponse;
import com.tejas.incidentplatform.dto.PagedResponse;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.exception.IncidentNotFoundException;
import com.tejas.incidentplatform.repository.IncidentRepository;
import com.tejas.incidentplatform.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.*;

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

    public IncidentResponse getIncidentById(Long id){
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException(id)); 
    
        return mapToResponse(incident);
    }

    public PagedResponse<IncidentResponse> getAllIncidents(
        int page,
        int size,
        String sortBy,
        String direction) {

    Sort.Direction sortDirection =
            direction.equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortDirection, sortBy));

    Page<Incident> incidentPage =
            incidentRepository.findAll(pageable);

    List<IncidentResponse> responses =
            incidentPage.getContent()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

    return new PagedResponse<>(
            responses,
            incidentPage.getNumber(),
            incidentPage.getSize(),
            incidentPage.getTotalElements(),
            incidentPage.getTotalPages(),
            incidentPage.isFirst(),
            incidentPage.isLast());
}
}
