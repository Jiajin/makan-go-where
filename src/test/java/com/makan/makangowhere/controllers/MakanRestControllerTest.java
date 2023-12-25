package com.makan.makangowhere.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makan.makangowhere.models.AcceptInviteRequest;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.CreatePersonRequest;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.GetMeetingRequest;
import com.makan.makangowhere.models.GetPersonRequest;
import com.makan.makangowhere.repository.PersonRepository;
import com.makan.makangowhere.services.MeetingService;
import com.makan.makangowhere.services.PersonService;
import com.makan.makangowhere.services.PlaceService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private final String saveMeetingUrl = "/api/v1/client/saveMeeting";
    private final String finalizeMeetingUrl = "/api/v1/client/finalizeMeeting";
    private final String createPlaceUrl = "/api/v1/client/createPlace";
    private final String acceptInviteUrl = "/api/v1/client/acceptInvite";
    private final String getPersonUrl = "/api/v1/client/getPerson";
    private final String getMeetingByIdUrl = "/api/v1/client/getMeetingById";

    @Test
    public void whenPostToSaveMeeting_BlankInput() throws Exception {
        // Given
        CreateMeetingRequest invalidRequest = new CreateMeetingRequest("", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(saveMeetingUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"));
    }

    @Test
    public void whenPostToFinalize_BlankInput() throws Exception {
        // Given
        CreateMeetingRequest invalidRequest = new CreateMeetingRequest("", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(finalizeMeetingUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meetingId").value("must not be blank"));
    }

    @Test
    public void whenPostToSavePerson_BlankInput() throws Exception {
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
    public void whenPostToSavePerson_InvalidEmail() throws Exception {
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
    public void whenPostToAcceptInvite_BlankInput() throws Exception {
        // Given
        AcceptInviteRequest invalidRequest = new AcceptInviteRequest("", "", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(acceptInviteUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meetingId").value("must not be blank"));
    }

    @Test
    public void whenPostToAcceptInvite_InvalidEmail() throws Exception {
        // Given
        AcceptInviteRequest invalidRequest = new AcceptInviteRequest("123", "nonsenseEmail", "123");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(acceptInviteUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must be a well-formed email address"));
    }

    @Test
    public void whenPostToGetPerson_BlankInput() throws Exception {
        // Given
        GetPersonRequest invalidRequest = new GetPersonRequest("");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(getPersonUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must not be blank"));
    }

    @Test
    public void whenPostToGetPerson_InvalidEmail() throws Exception {
        // Given
        GetPersonRequest invalidRequest = new GetPersonRequest("123");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(getPersonUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must be a well-formed email address"));
    }

    @Test
    public void whenPostToCreatePlace_BlankInput() throws Exception {
        // Given
        CreatePlaceRequest invalidRequest = new CreatePlaceRequest("", "", "", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(createPlaceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meetingId").value("must not be blank"));
    }

    @Test
    public void whenPostToGetMeetingById_BlankInput() throws Exception {
        // Given
        GetMeetingRequest invalidRequest = new GetMeetingRequest("");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post(getMeetingByIdUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("must not be blank"));
    }

}
