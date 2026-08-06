package com.tejas.incidentplatform.service;

import java.time.OffsetDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tejas.incidentplatform.dto.IncidentRequest;
import com.tejas.incidentplatform.dto.IncidentResponse;
import com.tejas.incidentplatform.dto.PagedResponse;
import com.tejas.incidentplatform.dto.UpdateIncidentStatusRequest;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.exception.IncidentNotFoundException;
import com.tejas.incidentplatform.exception.InvalidStatusTransitionException;
import com.tejas.incidentplatform.repository.IncidentRepository;

import jakarta.transaction.Transactional;

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

    //method to get paginated response
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

//method to update incident status alone using PATCH request

@Transactional
public IncidentResponse updateStatus(
        Long id,
        UpdateIncidentStatusRequest request) {

    Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new IncidentNotFoundException(id));

    IncidentStatus requestedStatus = request.getStatus();

    validateStatusTransition(
            incident.getStatus(),
            requestedStatus
    );

    incident.setStatus(requestedStatus);
    incident.setUpdatedAt(OffsetDateTime.now());

    return mapToResponse(incident);
}

private void validateStatusTransition(
        IncidentStatus currentStatus,
        IncidentStatus requestedStatus) {

    if (currentStatus == requestedStatus) {
        return;
    }

    boolean validTransition = switch (currentStatus) {
        case OPEN ->
                requestedStatus == IncidentStatus.INVESTIGATING;

        case INVESTIGATING ->
                requestedStatus == IncidentStatus.MITIGATED;

        case MITIGATED ->
                requestedStatus == IncidentStatus.RESOLVED;

        case RESOLVED ->
                false;
    };

    if (!validTransition) {
        throw new InvalidStatusTransitionException(
                currentStatus,
                requestedStatus
        );
    }
}

// to delet existing incident

@Transactional
public void deleteIncident(Long id) {
    Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new IncidentNotFoundException(id));

    incidentRepository.delete(incident);
}


}
