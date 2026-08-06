package com.tejas.incidentplatform.repository;

import com.tejas.incidentplatform.entity.Incident;
import com.tejas.incidentplatform.entity.IncidentStatus;
import com.tejas.incidentplatform.entity.Severity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
class IncidentRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void shouldSaveAndFindIncidentById() {
        OffsetDateTime now = OffsetDateTime.now();

        Incident incident = new Incident();
        incident.setTitle("Payment API failure");
        incident.setDescription(
                "Payment requests are returning HTTP 500"
        );
        incident.setServiceName("payment-service");
        incident.setSeverity(Severity.CRITICAL);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedAt(now);
        incident.setUpdatedAt(now);

        Incident savedIncident =
                incidentRepository.saveAndFlush(incident);

        Optional<Incident> result =
                incidentRepository.findById(savedIncident.getId());

        assertTrue(result.isPresent());
        assertEquals(
                "Payment API failure",
                result.get().getTitle()
        );
        assertEquals(
                Severity.CRITICAL,
                result.get().getSeverity()
        );
        assertEquals(
                IncidentStatus.OPEN,
                result.get().getStatus()
        );
    }
}