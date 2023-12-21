package com.makan.makangowhere.services;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.InvalidInputException;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;

import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final PersonRepository personRepository;

    public Meeting save(CreateMeetingRequest input) throws RecordNotFoundException, InvalidInputException {

        // Validation:
        if (input.getCreatedBy() == null || input.getCreatedBy().isEmpty()) {
            throw new InvalidInputException(errorMessages.PersonNotFound);
        }

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

            // Throw exception
            throw e;
        }
    }

    public Optional<Meeting> get(String meetingId) {
        return meetingRepository.findById(meetingId);
    }

}
