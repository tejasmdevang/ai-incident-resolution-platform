package com.tejas.incidentplatform.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tejas.incidentplatform.service.AuthService;
import com.tejas.incidentplatform.service.IncidentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.tejas.incidentplatform.service.AuthService;
import com.tejas.incidentplatform.service.IncidentService;
import org.springframework.context.annotation.Import;

@WebMvcTest(properties = {
    "security.jwt.secret=dGVzdC1qd3Qtc2VjcmV0LWtleS10aGF0LWlzLWxvbmc="
})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private IncidentService incidentService;

    @Test
    void shouldRejectUnauthenticatedRequestToIncidents() throws Exception {

        mockMvc.perform(
                get("/api/incidents")
        )
        .andExpect(
                status().isUnauthorized()
        );
    }
}