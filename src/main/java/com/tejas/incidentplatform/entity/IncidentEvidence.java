package com.tejas.incidentplatform.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "incident_evidence")
public class IncidentEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_id", nullable = false)
    private Long incidentId;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "observed_at", nullable = false)
    private OffsetDateTime observedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected IncidentEvidence() {
    }

    public IncidentEvidence(
            Long incidentId,
            String type,
            String source,
            String content,
            OffsetDateTime observedAt
    ) {
        this.incidentId = incidentId;
        this.type = type;
        this.source = source;
        this.content = content;
        this.observedAt = observedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getObservedAt() {
        return observedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}