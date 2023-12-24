package com.makan.makangowhere.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;
import com.makan.makangowhere.repository.PlaceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PersonRepository personRepository;
    private final MeetingRepository meetingRepository;

    private final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    public Place save(CreatePlaceRequest input) {

        // Check if meeting exists
        Optional<Meeting> meetingOptional = meetingRepository.findById(input.getMeetingId());
        if (!meetingOptional.isPresent()) {
            throw new RecordNotFoundException(errorMessages.MeetingNotFound);
        }
        Meeting meeting = meetingOptional.get();

        // Check if person is part of Meeting
        Optional<Person> personOptional = personRepository.findById(input.getCreatedBy());
        if (!personOptional.isPresent()) {
            throw new RecordNotFoundException(errorMessages.PersonNotFound);
        }
        Person person = personOptional.get();
        List<String> meetingList = person.getMeetingCsvList();
        if (meetingList == null || meetingList.size() == 0 || !meetingList.contains(meeting.getId())) {
            throw new RecordNotFoundException(errorMessages.NotInMeeting);
        }

        try {
            Place place = placeRepository.save(
                    new Place(input.getName(), input.getAddress(), input.getCreatedBy(), meetingOptional.get()));
            return place;
        } catch (Exception e) {
            // Log Error
            logger.trace(e.getStackTrace().toString());
            logger.info(e.getMessage());

            throw e;
        }
    }
}
