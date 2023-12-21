package com.makan.makangowhere.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    public void whenPostToCreatePerson_BlankInput() throws Exception {
        // Given
        CreatePersonRequest invalidRequest = new CreatePersonRequest("", "");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/client/savePerson")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/client/savePerson")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must be a well-formed email address"));
    }

}
