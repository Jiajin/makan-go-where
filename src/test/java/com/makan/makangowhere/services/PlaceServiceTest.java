package com.makan.makangowhere.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PlaceRepository;

import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceService placeService;

    private static Stream<Arguments> createTestCases() {
        return Stream.of(
                Arguments.of("Valid Input", "address", "personId", "meetingId", true, false),
                Arguments.of("Invalid Input", "address", "personId", "meetingId", false, true)

        );
    }

    @ParameterizedTest
    @MethodSource("createTestCases")
    @MockitoSettings(strictness = Strictness.LENIENT) // To allow for multiple test cases w single Parameterized test
    public void testSave(String testName, String address, String personId, String meetingId, boolean meetingExist,
            boolean exception) {

        // Given
        CreatePlaceRequest input = new CreatePlaceRequest(testName, address, personId, meetingId);

        Meeting existingMeeting = new Meeting(testName, personId);
        existingMeeting.setId(meetingId);

        if (meetingExist) {
            when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(existingMeeting));
        } else {
            when(meetingRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        }
        when(placeRepository.save(any())).thenReturn(new Place(testName, address, personId, existingMeeting));

        // When
        try {
            Place savedPlace = placeService.save(input);
            assertEquals(testName, savedPlace.getName());
            assertEquals(address, savedPlace.getAddress());
            assertEquals(false, savedPlace.getChosen());
            assertEquals(personId, savedPlace.getCreatedBy());
            assertEquals(testName, savedPlace.getMeeting().getName());
            assertEquals(personId, savedPlace.getMeeting().getCreatedBy());
        } catch (RecordNotFoundException e) {
            // check expected exception
            if (exception) {
                assertEquals(e.getMessage(), "The makan session does not exist");

            } else
                fail("Unexpected exception: " + e.getMessage());
        }

    }
}
