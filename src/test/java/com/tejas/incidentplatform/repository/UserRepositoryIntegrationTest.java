package com.tejas.incidentplatform.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import com.tejas.incidentplatform.entity.Role;
import com.tejas.incidentplatform.repository.UserRepository;
import com.tejas.incidentplatform.entity.User;


@DataJpaTest
@Testcontainers
public class UserRepositoryIntegrationTest {

@Container
@ServiceConnection
    static PostgreSQLContainer postgres =
        new PostgreSQLContainer("postgres:17-alpine");


@Autowired
private UserRepository userRepository;

@Test
void shouldFindUserByEmail() {
    User user = new User();
    user.setName("Tejas Devang");
    user.setEmail("tejas@example.com");
    user.setPasswordHash("$2a$10$temporaryTestHash");
    user.setRole(Role.ENGINEER);
    user.setCreatedAt(OffsetDateTime.now());

    userRepository.saveAndFlush(user);

    Optional<User> result =
            userRepository.findByEmail("tejas@example.com");

    assertTrue(result.isPresent());
    assertEquals("Tejas Devang", result.get().getName());
    assertEquals(Role.ENGINEER, result.get().getRole());
}


}
