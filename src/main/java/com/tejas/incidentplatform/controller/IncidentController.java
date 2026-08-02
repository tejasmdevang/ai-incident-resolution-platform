package com.tejas.incidentplatform.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejas.incidentplatform.dto.IncidentRequest;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.service.IncidentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService){
        this.incidentService = incidentService;

    }

@PostMapping
public Incident createIncident(
        @Valid @RequestBody IncidentRequest request) {

    return incidentService.createIncident(request);

}

}
