package com.makan.makangowhere.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.PersonRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void setup() {
        // Clear DB
        personRepository.deleteAll();

        Person person = new Person("name", "123@email.com");
        personRepository.save(person);

    }

    @Test
    public void testIssueToken() {

        // Given
        String given = "123@email.com";

        // When
        String actual = authService.issueToken(given);

        // Then
        assertNotNull(actual);
        // Verify the JWT token
        Jws<Claims> parsedJwt = Jwts.parserBuilder()
                .setSigningKey(authService.decodedKey)
                .build()
                .parseClaimsJws(actual);
        assertEquals(parsedJwt.getBody().getSubject(), given);

    }
}
