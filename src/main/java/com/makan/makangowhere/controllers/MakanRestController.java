package com.makan.makangowhere.controllers;

import com.makan.makangowhere.exceptions.InvalidInputException;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.CreateMeetingResponse;
import com.makan.makangowhere.models.CreatePersonRequest;
import com.makan.makangowhere.models.GetMeetingRequestModel;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.services.MeetingService;
import com.makan.makangowhere.services.PersonService;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class MakanRestController {
    private final MeetingService meetingService;
    private final PersonService personService;
    // private final PlaceService placeService;

    @PostMapping(value = "/saveMeeting", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<CreateMeetingResponse> saveMeeting(@RequestBody CreateMeetingRequest input) {

        CreateMeetingResponse response = new CreateMeetingResponse();
        try {
            Meeting meeting = meetingService.save(input);
            // if (meeting == null) {
            // response.setErrorMessage("Invalid Person");
            // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            // }
            response.setMeeting(meeting);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (RecordNotFoundException e) {
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (InvalidInputException e) {
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            response.setErrorMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/savePerson", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Person> savePerson(@RequestBody CreatePersonRequest input) {
        Person person = new Person(input.getName(), input.getEmail());
        Person savedPerson = personService.save(person);

        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PostMapping(value = "/getMeetingById", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CreateMeetingResponse> getMeetingById(@RequestBody GetMeetingRequestModel input) {
        CreateMeetingResponse response = new CreateMeetingResponse();
        try {
            Optional<Meeting> meetingOptional = meetingService.get(input.getId());

            if (meetingOptional.isPresent()) {
                Meeting meeting = meetingOptional.get();
                response.setMeeting(meeting);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setErrorMessage("Meeting not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
