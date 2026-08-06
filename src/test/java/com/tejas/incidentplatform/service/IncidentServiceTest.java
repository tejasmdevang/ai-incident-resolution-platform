package com.tejas.incidentplatform.service;

import com.tejas.incidentplatform.dto.IncidentResponse;
import com.tejas.incidentplatform.dto.UpdateIncidentStatusRequest;
import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.entity.Severity;
import com.tejas.incidentplatform.exception.IncidentNotFoundException;
import com.tejas.incidentplatform.exception.InvalidStatusTransitionException;
import com.tejas.incidentplatform.repository.IncidentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        incidentService = new IncidentService(incidentRepository);
    }

    @Test
    void shouldUpdateStatusFromOpenToInvestigating() {
        Incident incident = createIncident(IncidentStatus.OPEN);

        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.INVESTIGATING);

        when(incidentRepository.findById(1L))
                .thenReturn(Optional.of(incident));

        IncidentResponse response =
                incidentService.updateStatus(1L, request);

        assertEquals(
                IncidentStatus.INVESTIGATING,
                response.getStatus()
        );

        assertEquals(
                IncidentStatus.INVESTIGATING,
                incident.getStatus()
        );

        verify(incidentRepository).findById(1L);
    }

    @Test
    void shouldRejectTransitionFromOpenToResolved() {
        Incident incident = createIncident(IncidentStatus.OPEN);

        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.RESOLVED);

        when(incidentRepository.findById(1L))
                .thenReturn(Optional.of(incident));

        InvalidStatusTransitionException exception =
                assertThrows(
                        InvalidStatusTransitionException.class,
                        () -> incidentService.updateStatus(1L, request)
                );

        assertEquals(
                "Cannot change incident status from OPEN to RESOLVED",
                exception.getMessage()
        );

        assertEquals(
                IncidentStatus.OPEN,
                incident.getStatus()
        );

        verify(incidentRepository).findById(1L);
        verify(incidentRepository, never()).save(incident);
    }

    @Test
    void shouldThrowExceptionWhenIncidentDoesNotExist() {
        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.INVESTIGATING);

        when(incidentRepository.findById(99L))
                .thenReturn(Optional.empty());

        IncidentNotFoundException exception =
                assertThrows(
                        IncidentNotFoundException.class,
                        () -> incidentService.updateStatus(99L, request)
                );

        assertEquals(
                "Incident not found with id: 99",
                exception.getMessage()
        );

        verify(incidentRepository).findById(99L);
    }

        @Test
        void shouldDeleteExistingIncident() {
        Long incidentId = 1L;
        Incident incident = new Incident();
        incident.setId(incidentId);

        when(incidentRepository.findById(incidentId))
                .thenReturn(Optional.of(incident));

        incidentService.deleteIncident(incidentId);

        verify(incidentRepository).findById(incidentId);
        verify(incidentRepository).delete(incident);
}
@Test
void shouldThrowExceptionWhenDeletingMissingIncident() {
    Long incidentId = 999L;

    when(incidentRepository.findById(incidentId))
            .thenReturn(Optional.empty());

    assertThrows(
            IncidentNotFoundException.class,
            () -> incidentService.deleteIncident(incidentId)
    );

    verify(incidentRepository).findById(incidentId);
    verify(incidentRepository, never()).delete(any());
}

    private Incident createIncident(IncidentStatus status) {
        Incident incident = new Incident();

        incident.setId(1L);
        incident.setTitle("Order API failure");
        incident.setDescription(
                "Order API is returning HTTP 500"
        );
        incident.setServiceName("order-service");
        incident.setSeverity(Severity.HIGH);
        incident.setStatus(status);
        incident.setCreatedAt(OffsetDateTime.now());
        incident.setUpdatedAt(OffsetDateTime.now());

        return incident;
    }
}