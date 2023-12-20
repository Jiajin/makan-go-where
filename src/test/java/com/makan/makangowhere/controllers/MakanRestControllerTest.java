package com.makan.makangowhere.controllers;

import com.makan.makangowhere.models.Meeting;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MakanRestControllerTest {

    @Test
    void contextLoads() {
    }

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private final String url = "/api/v1/client/save";

    @Test
    public void postMeetingSuccessTest() {
        Meeting meeting = new Meeting("123", "9489b7aa-c7bc-4975-9a04-57844dab1d6a");
        HttpEntity<Meeting> httpEntity = new HttpEntity<>(meeting);
        ResponseEntity<Meeting> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Meeting.class);
        System.out.println(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
