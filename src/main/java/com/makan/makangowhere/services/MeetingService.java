package com.makan.makangowhere.services;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;

import lombok.AllArgsConstructor;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final PersonRepository personRepository;

    private final Logger logger = LoggerFactory.getLogger(MeetingService.class);

    public Meeting save(CreateMeetingRequest input) throws RecordNotFoundException {

        // Check if person exists
        Optional<Person> person = personRepository.findById(input.getCreatedBy());
        if (!person.isPresent()) {
            throw new RecordNotFoundException(errorMessages.PersonNotFound);
        }
        try {
            Meeting meeting = meetingRepository.save(new Meeting(input.getName(), input.getCreatedBy()));
            return meeting;
        } catch (Exception e) {
            // Log Error
            logger.trace(e.getStackTrace().toString());
            logger.info(e.getMessage());

            throw e;
        }
    }

    public Meeting get(String meetingId) {
        try {
            Optional<Meeting> retrieved = meetingRepository.findById(meetingId);

            if (!retrieved.isPresent()) {
                throw new RecordNotFoundException(errorMessages.RecordNotFound);
            }

            return retrieved.get();
        } catch (Exception e) {
            // Log Error
            logger.trace(e.getStackTrace().toString());
            logger.info(e.getMessage());

            throw e;
        }
    }

}
