package com.tejas.incidentplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejas.incidentplatform.dto.IncidentRequest;
import com.tejas.incidentplatform.dto.IncidentResponse;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.service.IncidentService;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService){
        this.incidentService = incidentService;

    }

@PostMapping
public ResponseEntity<IncidentResponse> createIndicent(
        @Valid @RequestBody IncidentRequest request) {

    IncidentResponse response = incidentService.createIncident(request);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);

}

}
