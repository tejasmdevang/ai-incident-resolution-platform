package com.tejas.incidentplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tejas.incidentplatform.entity.Incident;


public interface IncidentRepository extends JpaRepository<Incident, Long> {

}
