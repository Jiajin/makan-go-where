package com.makan.makangowhere.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Meeting.MeetingStatus;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;
import com.makan.makangowhere.repository.PlaceRepository;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PlaceServiceTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PlaceService placeService;

    private static String personId;
    private static String meetingId;

    @BeforeEach
    public void setup() {
        // Clear DB
        placeRepository.deleteAll();
        meetingRepository.deleteAll();
        personRepository.deleteAll();

        Person person = new Person("name", "123@email.com");
        personRepository.save(person);
        personId = person.getId();

        Meeting meeting = new Meeting("meetingName", person.getId(), MeetingStatus.ACTIVE);
        meetingRepository.save(meeting);

        List<String> meetingList = new ArrayList<String>();
        meetingList.add(meeting.getId());
        person.setMeetingCsvList(meetingList);
        personRepository.save(person);

        meetingId = meeting.getId();

    }

    private static Stream<Arguments> createTestCases() {
        return Stream.of(
                Arguments.of(new CreatePlaceRequest("placeName", "address", "createdBy", "meetingId"),
                        true, true, true, true, ""),
                Arguments.of(new CreatePlaceRequest("placeName", "address", "createdBy", "meetingId"),
                        false, true, true, true,
                        "The makan session does not exist"),
                Arguments.of(new CreatePlaceRequest("placeName", "address", "createdBy", "meetingId"),
                        true, false, true, true,
                        "The makan location has been chosen!"),
                Arguments.of(new CreatePlaceRequest("placeName", "address", "createdBy", "meetingId"),
                        true, true, false, true,
                        "The user trying to create the session does not exist"),
                Arguments.of(new CreatePlaceRequest("placeName", "address", "createdBy", "meetingId"),
                        true, true, true, false,
                        "The user trying to create the location is not part of this makan session")

        );
    }

    @ParameterizedTest
    @MethodSource("createTestCases")
    @MockitoSettings(strictness = Strictness.LENIENT) // To allow for multiple test cases w single Parameterized test
    public void testSave(CreatePlaceRequest input, boolean meetingExist, boolean meetingActive, boolean isPersonExist,
            boolean isPersonInvited, String exMessage) {

        // Given
        if (meetingExist) {
            input.setMeetingId(meetingId);
            Optional<Meeting> meetingOptional = meetingRepository.findById(input.getMeetingId());
            assertEquals(meetingOptional.isPresent(), true);

            if (isPersonExist) {
                input.setCreatedBy(personId);

                if (!isPersonInvited) {
                    Optional<Person> personOptional = personRepository.findById(input.getCreatedBy());
                    assertEquals(personOptional.isPresent(), true);
                    Person person = personOptional.get();
                    person.setMeetingCsvList(null);
                    personRepository.save(person);
                }
            }
        }

        // When
        try {
            Place savedPlace = placeService.save(input);
            assertEquals(input.getName(), savedPlace.getName());
            assertEquals(input.getAddress(), savedPlace.getAddress());
            assertEquals(false, savedPlace.getChosen());
            assertEquals(input.getCreatedBy(), savedPlace.getCreatedBy());

        } catch (RecordNotFoundException e) {
            // check expected exception
            if (!exMessage.equals("")) {
                assertEquals(exMessage, e.getMessage());

            } else
                fail("Unexpected exception: " + e.getMessage());
        }
    }
}
