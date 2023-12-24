package com.makan.makangowhere.services;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.FinalizeMeetingRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.models.Meeting.MeetingStatus;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;
import com.makan.makangowhere.repository.PlaceRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final PersonRepository personRepository;
    private final PlaceRepository placeRepository;

    private final Logger logger = LoggerFactory.getLogger(MeetingService.class);

    @Transactional
    public Meeting save(CreateMeetingRequest input) throws RecordNotFoundException {

        // Check if person exists
        Optional<Person> personOptional = personRepository.findById(input.getCreatedBy());
        if (!personOptional.isPresent()) {
            throw new RecordNotFoundException(errorMessages.PersonNotFound);
        }

        Person person = personOptional.get();

        try {
            Meeting meeting = meetingRepository
                    .save(new Meeting(input.getName(), input.getCreatedBy(), MeetingStatus.ACTIVE));

            // Update person
            List<String> oldMeetingList = person.getMeetingCsvList();
            if (oldMeetingList == null || oldMeetingList.size() == 0) {
                oldMeetingList = new ArrayList<>();
            }
            oldMeetingList.add(meeting.getId());
            person.setMeetingCsvList(oldMeetingList);
            personRepository.save(person);

            return meeting;
        } catch (Exception e) {
            // Log Error
            logger.trace(e.getStackTrace().toString());
            logger.info(e.getMessage());

            throw e;
        }
    }

    @Transactional
    public Meeting finalize(FinalizeMeetingRequest input) throws RecordNotFoundException {

        // Check if host is the one initiating
        Optional<Meeting> meetingOptional = meetingRepository.findById(input.getMeetingId());
        if (!meetingOptional.isPresent()) {
            throw new RecordNotFoundException(errorMessages.MeetingNotFound);
        }
        Meeting meeting = meetingOptional.get();
        if (meeting.getStatus().equals(MeetingStatus.FINAL))
            throw new RecordNotFoundException(errorMessages.MeetingOver);
        if (!meeting.getCreatedBy().equals(input.getCreatedBy()))
            throw new RecordNotFoundException(errorMessages.NotMeetingHost);

        // Update values
        meeting.setStatus(MeetingStatus.FINAL);
        List<Place> placesList = meeting.getPlaces();
        if (placesList.size() == 0) {
            throw new RecordNotFoundException(errorMessages.NoMeetingLocations);
        }

        // Select a random place from the list
        Random random = new Random();
        int randomIndex = random.nextInt(placesList.size());
        Place chosenPlace = placesList.get(randomIndex);
        chosenPlace.setChosen(true);

        // Save
        try {
            Meeting savedMeeting = meetingRepository.save(meeting);
            placeRepository.save(chosenPlace);
            return savedMeeting;
        } catch (Exception e) {
            // Log Error
            logger.trace(e.getStackTrace().toString());
            logger.info(e.getMessage());

            throw e;
        }
    }

    public Meeting get(String meetingId) {
        try {
            Optional<Meeting> meetingOptional = meetingRepository.findById(meetingId);

            if (!meetingOptional.isPresent()) {
                throw new RecordNotFoundException(errorMessages.RecordNotFound);
            }

            return meetingOptional.get();
        } catch (Exception e) {
            // Log Error
            logger.trace(e.getStackTrace().toString());
            logger.info(e.getMessage());

            throw e;
        }
    }

}
