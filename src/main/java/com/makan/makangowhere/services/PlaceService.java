package com.makan.makangowhere.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PlaceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final MeetingRepository meetingRepository;

    public Place save(CreatePlaceRequest input) {

        // Validation

        // Check if meeting exists
        Optional<Meeting> meeting = meetingRepository.findById(input.getMeetingId());
        if (!meeting.isPresent()) {
            throw new RecordNotFoundException(errorMessages.PersonNotFound); // TODO: update exception msg
        }
        try {
            Place place = placeRepository.save(
                    new Place(input.getName(), input.getAddress(), input.getCreatedBy(), meeting.get()));
            return place;
        } catch (Exception e) {
            // Log Error
            e.printStackTrace();
            // Throw exception
            throw e;
        }
    }
}
