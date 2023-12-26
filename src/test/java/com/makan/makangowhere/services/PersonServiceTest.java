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
import com.makan.makangowhere.models.AcceptInviteRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Meeting.MeetingStatus;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;
import com.makan.makangowhere.repository.PlaceRepository;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    private static String meetingId;
    private static String personEmail;

    @BeforeEach
    public void setup() {
        // Clear DB
        placeRepository.deleteAll();
        meetingRepository.deleteAll();
        personRepository.deleteAll();

        Person person = new Person("name", "123@email.com");
        personRepository.save(person);
        personEmail = person.getEmail();

        Meeting meeting = new Meeting("meetingName", person.getId(), MeetingStatus.ACTIVE);
        meetingRepository.save(meeting);

        List<String> meetingList = new ArrayList<String>();
        meetingList.add(meeting.getId());
        person.setMeetingCsvList(meetingList);
        personRepository.save(person);

        meetingId = meeting.getId();

    }

    private static Stream<Arguments> savePersonMeetingTestCases() {
        return Stream.of(
                Arguments.of(new AcceptInviteRequest("personName", "email", "meetingId"), true,
                        true, ""),
                Arguments.of(new AcceptInviteRequest("personName", "email", "meetingId"), false,
                        true, "The makan session does not exist"),
                Arguments.of(new AcceptInviteRequest("personName", "email", "meetingId"), true,
                        false, "The makan location has been chosen!")

        );
    }

    @ParameterizedTest
    @MethodSource("savePersonMeetingTestCases")
    @MockitoSettings(strictness = Strictness.LENIENT) // To allow for multiple test cases w single Parameterized test
    public void testSave(AcceptInviteRequest input, boolean meetingExist, boolean meetingActive, String exMessage) {

        // Given
        if (meetingExist) {
            input.setMeetingId(meetingId);
            Optional<Meeting> meetingOptional = meetingRepository.findById(input.getMeetingId());
            assertEquals(meetingOptional.isPresent(), true);

        }

        // When
        try {
            Person savedPerson = personService.savePersonMeeting(input);

            // Then
            assertEquals(input.getName(), savedPerson.getName());
            assertEquals(input.getEmail(), savedPerson.getEmail());

            assertEquals(input.getMeetingId(), savedPerson.getMeetingCsvList().get(0));

        } catch (RecordNotFoundException e) {
            // check expected exception
            if (!exMessage.equals("")) {
                assertEquals(exMessage, e.getMessage());

            } else
                fail("Unexpected exception: " + e.getMessage());
        }
    }

    private static Stream<Arguments> getTestCases() {
        return Stream.of(
                Arguments.of("personId", true, ""),
                Arguments.of("personId", false, "The user does not exist"));
    }

    @ParameterizedTest
    @MethodSource("getTestCases")
    public void testGet(String email, boolean personExist, String exMessage) {

        // Given
        if (personExist)
            email = personEmail;

        // When
        try {
            Person retrieved = personService.get(email);
            // Then
            assertNotNull(retrieved);
            List<Meeting> meetingList = retrieved.getMeetingList();
            assertNotNull(meetingList);
            assertEquals(1, meetingList.size());
            assertEquals("meetingName", meetingList.get(0).getName());
        } catch (RecordNotFoundException e) {
            // check expected exception
            if (!exMessage.equals("")) {
                assertEquals(e.getMessage(), exMessage);

            } else
                fail("Unexpected exception: " + e.getMessage());
        }
    }

}
