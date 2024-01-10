package com.makan.makangowhere.services;

import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.PersonRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    private final PersonRepository personRepository;
    private final long jwtExpirationInSec;
    protected final byte[] decodedKey;

    public AuthService(PersonRepository personRepository,
            @Value("${jwt.expirationInSec}") long jwtExpirationInSec,
            @Value("${jwt.secret}") String jwtSecret) {
        this.personRepository = personRepository;
        this.jwtExpirationInSec = jwtExpirationInSec;
        this.decodedKey = Base64.getDecoder().decode(jwtSecret);
    }

    public String issueToken(String email) {

        Optional<Person> personOptional = personRepository.findByEmail(email);

        if (!personOptional.isPresent())
            throw new RecordNotFoundException(errorMessages.PersonNotFound);

        return generateJwtToken(personOptional.get());

    }

    private String generateJwtToken(Person user) {
        Instant now = Instant.now();
        Instant expiryDate = now.plusSeconds(jwtExpirationInSec);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .signWith(Keys.hmacShaKeyFor(decodedKey), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromToken(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(decodedKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims.getSubject();
    }

    // // Generate a secret key for HMAC-SHA512
    // SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // // Convert the SecretKey to a byte array
    // byte[] decodedKey = key.getEncoded();

    // // Encode the byte array to a base64 string
    // String jwtSecret = Base64.getEncoder().encodeToString(decodedKey);

    // return jwtSecret;
}
