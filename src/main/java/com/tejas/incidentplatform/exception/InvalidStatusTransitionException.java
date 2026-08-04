package com.tejas.incidentplatform.exception;

import com.tejas.incidentplatform.entity.IncidentStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(
            IncidentStatus currentStatus,
            IncidentStatus requestedStatus) {

        super("Cannot change incident status from "
                        + currentStatus
                        + " to "
                        + requestedStatus
        );
    }
}