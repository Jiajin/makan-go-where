package com.makan.makangowhere.controllers;

import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.CreatePersonRequest;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.GetMeetingRequestModel;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.services.MeetingService;
import com.makan.makangowhere.services.PersonService;
import com.makan.makangowhere.services.PlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final PlaceService placeService;

    private final Logger logger = LoggerFactory.getLogger(MakanRestController.class);

    @PostMapping(value = "/saveMeeting", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Meeting> saveMeeting(@Valid @RequestBody CreateMeetingRequest input) {

        logger.info("Received: {}", input);
        Meeting meeting = meetingService.save(input);

        return new ResponseEntity<>(meeting, HttpStatus.CREATED);

    }

    @PostMapping(value = "/savePerson", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Person> savePerson(@Valid @RequestBody CreatePersonRequest input) {

        logger.info("Received: {}", input);
        Person person = new Person(input.getName(), input.getEmail());
        Person savedPerson = personService.save(person);

        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PostMapping(value = "/getMeetingById", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Meeting> getMeetingById(@Valid @RequestBody GetMeetingRequestModel input) {

        logger.info("Received: {}", input);
        Meeting meeting = meetingService.get(input.getId());

        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @PostMapping(value = "/createPlace", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Place> createPlace(@Valid @RequestBody CreatePlaceRequest input) {

        logger.info("Received: {}", input);
        Place place = placeService.save(input);

        return new ResponseEntity<>(place, HttpStatus.CREATED);
        // try {

        // response.setPlace(place);
        // return new ResponseEntity<>(response, HttpStatus.CREATED);

        // } catch (RecordNotFoundException e) {
        // response.setErrorMessage(e.getMessage());
        // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        // } catch (InvalidInputException e) {
        // response.setErrorMessage(e.getMessage());
        // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        // } catch (Exception e) {
        // response.setErrorMessage("Internal Server Error");
        // return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        // }
    }

}
