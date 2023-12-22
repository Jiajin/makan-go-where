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

import com.makan.makangowhere.exceptions.InvalidInputException;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;

import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

	@Mock
	private MeetingRepository meetingRepository;

	@Mock
	private PersonRepository personRepository;

	@InjectMocks
	private MeetingService meetingService;

	private static Stream<Arguments> saveTestCases() {
		return Stream.of(
				Arguments.of("Valid Input", "existingPersonId", true, false),
				Arguments.of("Invalid Input", null, true, true),
				Arguments.of("PersonNotExist", "NotExistPersonId", false, true));
	}

	@ParameterizedTest
	@MethodSource("saveTestCases")
	@MockitoSettings(strictness = Strictness.LENIENT) // To allow for multiple test cases w single Parameterized test
	public void testSave(String testName, String personId, boolean personExist, boolean exception) {

		// Given
		CreateMeetingRequest input = new CreateMeetingRequest(testName, personId);

		Person existingPerson = new Person("name", "email");
		existingPerson.setId(personId);

		if (personExist) {
			when(personRepository.findById(any())).thenReturn(Optional.of(existingPerson));
		} else {
			when(personRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		}
		when(meetingRepository.save(any())).thenReturn(new Meeting(testName, personId));

		// When
		try {
			Meeting savedMeeting = meetingService.save(input);
			assertEquals(testName, savedMeeting.getName());
			assertEquals(personId, savedMeeting.getCreatedBy());
			assertEquals("ACTIVE", savedMeeting.getStatus().toString());
			assertEquals(null, savedMeeting.getPlaces());
		} catch (RecordNotFoundException | InvalidInputException e) {
			// check expected exception
			if (exception) {
				assertEquals(e.getMessage(), "The user trying to create the session does not exist");

			} else
				fail("Unexpected exception: " + e.getMessage());
		}
	}

	private static Stream<Arguments> getTestCases() {
		return Stream.of(
				Arguments.of("Valid Input", true, false),
				Arguments.of("Invalid Input", false, true));
	}

	@ParameterizedTest
	@MethodSource("getTestCases")
	@MockitoSettings(strictness = Strictness.LENIENT) // To allow for multiple test cases w single Parameterized test
	public void testGet(String meetingId, boolean meetingExist, boolean exception) {

		Meeting existingMeeting = new Meeting("anything", "anything");
		// Given
		if (meetingExist) {
			when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(existingMeeting));
		} else {
			when(meetingRepository.findById(meetingId)).thenReturn(Optional.ofNullable(null));
		}

		// When
		try {
			Meeting retrieved = meetingService.get(meetingId);
			// Then
			assertNotNull(retrieved);
		} catch (RecordNotFoundException | InvalidInputException e) {
			// check expected exception
			if (exception) {
				assertEquals(e.getMessage(), "The specified record does not exist");

			} else
				fail("Unexpected exception: " + e.getMessage());
		}
	}

}
