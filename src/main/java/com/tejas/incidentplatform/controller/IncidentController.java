package com.tejas.incidentplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tejas.incidentplatform.dto.PagedResponse;
import com.tejas.incidentplatform.dto.UpdateIncidentStatusRequest;

import org.springframework.web.bind.annotation.RequestParam;

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

@GetMapping("/{id}")
public ResponseEntity<IncidentResponse> getIncidentById(
    @PathVariable Long id){

        IncidentResponse response = incidentService.getIncidentById(id);

        return ResponseEntity.ok(response);
    }

@PostMapping
public ResponseEntity<IncidentResponse> createIndicent(
        @Valid @RequestBody IncidentRequest request) {

    IncidentResponse response = incidentService.createIncident(request);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);

}

//paginated response

@GetMapping
public ResponseEntity<PagedResponse<IncidentResponse>> getAllIncidents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {

    PagedResponse<IncidentResponse> response =
            incidentService.getAllIncidents(
                    page,
                    size,
                    sortBy,
                    direction);

    return ResponseEntity.ok(response);
}

// for updating the status of incident alone using PATCH request

@PatchMapping("/{id}/status")
public ResponseEntity<IncidentResponse>
updateStatus(

@PathVariable Long id,

@Valid
@RequestBody
UpdateIncidentStatusRequest request){

    return ResponseEntity.ok(

            incidentService.updateStatus(id, request)

    );

}



}
