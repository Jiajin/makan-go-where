package com.makan.makangowhere.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.CreatePersonRequest;
import com.makan.makangowhere.repository.PersonRepository;
import com.makan.makangowhere.services.MeetingService;
import com.makan.makangowhere.services.PersonService;
import com.makan.makangowhere.services.PlaceService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(MakanRestController.class)
public class MakanRestControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonService personService;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private PlaceService placeService;

    @Autowired
    private MockMvc mockMvc;

    private final String savePersonUrl = "/api/v1/client/savePerson";
    private final String createMeetingUrl = "/api/v1/client/saveMeeting";

    @Test
    public void whenPostToCreatePerson_BlankInput() throws Exception {
        // Given
        CreatePersonRequest invalidRequest = new CreatePersonRequest("", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(savePersonUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"));
    }

    @Test
    public void whenPostToCreatePerson_InvalidEmail() throws Exception {
        // Given
        CreatePersonRequest invalidRequest = new CreatePersonRequest("123", "123");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(savePersonUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must be a well-formed email address"));
    }

    @Test
    public void whenPostToCreateMeeting_BlankInput() throws Exception {
        // Given
        CreateMeetingRequest invalidRequest = new CreateMeetingRequest("", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(createMeetingUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"));
    }

    // @Test
    // public void TestCreateMeetingResponse() {
    // CreateMeetingResponse response = new CreateMeetingResponse();

    // Meeting meeting = new Meeting("123", "123");

    // response.setMeeting(meeting);
    // Assertions.assertEquals(response.getMeeting().getName(), "123");

    // }
    // @Autowired
    // protected TestRestTemplate testRestTemplate;

    // private final String url = "/api/v1/client/save";

    // @Test
    // public void postMeetingSuccessTest() {
    // Meeting meeting = new Meeting("123", "9489b7aa-c7bc-4975-9a04-57844dab1d6a");
    // HttpEntity<Meeting> httpEntity = new HttpEntity<>(meeting);
    // ResponseEntity<Meeting> response = testRestTemplate.exchange(url,
    // HttpMethod.POST, httpEntity, Meeting.class);
    // System.out.println(response.getStatusCode());
    // Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    // }
}
