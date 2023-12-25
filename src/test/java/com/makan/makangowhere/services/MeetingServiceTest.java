package com.makan.makangowhere.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MeetingServiceTest {

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private MeetingService meetingService;

	private static String personId;
	private static String meetingId;

	@BeforeEach
	public void setup() {
		// Clear DB
		placeRepository.deleteAll();
		personRepository.deleteAll();
		meetingRepository.deleteAll();

		Person person = new Person("name", "123@email.com");
		personRepository.save(person);
		personId = person.getId();

		Person person2 = new Person("name", "1234@email.com");
		personRepository.save(person2);

		Meeting meeting = new Meeting("meetingName", person2.getId(), MeetingStatus.ACTIVE);
		meetingRepository.save(meeting);

		List<Place> placesList = new ArrayList<>();
		placesList.add(new Place("1", "1", person.getId(), meeting));
		placesList.add(new Place("2", "2", person2.getId(), meeting));

		placesList.forEach(place -> placeRepository.save(place));
		meeting.setPlaces(placesList);
		meetingRepository.save(meeting);
		meetingId = meeting.getId();

	}

	private static Stream<Arguments> saveTestCases() {
		return Stream.of(
				Arguments.of(new CreateMeetingRequest("meetingName", "createdBy"), true, ""),
				Arguments.of(new CreateMeetingRequest("meetingName", "createdBy"), false,
						"The user does not exist")

		);
	}

	@ParameterizedTest
	@MethodSource("saveTestCases")
	public void testSave(CreateMeetingRequest input, boolean personExists, String exceptionMessage) {

		// Given
		if (personExists) {
			Optional<Person> retrieved = personRepository.findById(personId);
			assertEquals(true, retrieved.isPresent());
			Person person = retrieved.get();
			input.setCreatedBy(person.getId());
		}

		// When
		try {
			Meeting savedMeeting = meetingService.save(input);

			// Then
			assertEquals(input.getName(), savedMeeting.getName());
			assertEquals(input.getCreatedBy(), savedMeeting.getCreatedBy());
			assertEquals("ACTIVE", savedMeeting.getStatus().toString());
			assertEquals(null, savedMeeting.getPlaces());

			Optional<Person> retrieved = personRepository.findById(input.getCreatedBy());
			assertEquals(true, retrieved.isPresent());
			Person person = retrieved.get();

			assertEquals(savedMeeting.getId(), person.getMeetingCsvList().get(0));

		} catch (RecordNotFoundException e) {
			// check expected exception
			if (!exceptionMessage.equals("")) {
				assertEquals(e.getMessage(), exceptionMessage);

			} else
				fail("Unexpected exception: " + e.getMessage());
		}

	}

	private static Stream<Arguments> finalizeTestCases() {
		return Stream.of(
				Arguments.of(new FinalizeMeetingRequest("meetingId", "personId"), true, true,
						true, true, ""),
				Arguments.of(new FinalizeMeetingRequest("meetingId", "personId"), false,
						true, true, true,
						"The makan session does not exist"),
				Arguments.of(new FinalizeMeetingRequest("meetingId", "personId"), true,
						false, true, true,
						"The makan location has been chosen!"),
				Arguments.of(new FinalizeMeetingRequest("meetingId", "personId"), true, true,
						false, true,
						"Only the makan host can end the session"),
				Arguments.of(new FinalizeMeetingRequest("meetingId", "personId"), true, true,
						true, false,
						"There are no submitted locations for this makan session")

		);
	}

	@ParameterizedTest
	@MethodSource("finalizeTestCases")
	public void testFinalize(FinalizeMeetingRequest input, boolean meetingExist, boolean isActive, boolean isHost,
			boolean hasPlaces, String exMessage) {

		// Given
		if (meetingExist) {
			input.setMeetingId(meetingId);
			Optional<Meeting> meetingOptional = meetingRepository.findById(input.getMeetingId());
			assertEquals(meetingOptional.isPresent(), true);
			Meeting existingMeeting = meetingOptional.get();

			if (isHost) {
				input.setCreatedBy(existingMeeting.getCreatedBy());
			}
			if (!isActive) {
				existingMeeting.setStatus(MeetingStatus.FINAL);
			}
			if (!hasPlaces) {
				// Clear places in DB
				List<Place> placesList = existingMeeting.getPlaces();
				placeRepository.deleteAll(placesList);
				existingMeeting.setPlaces(null);
			}
			existingMeeting = meetingRepository.save(existingMeeting);

		}

		// When
		try {
			Meeting savedMeeting = meetingService.finalize(input);

			// Then
			assertEquals("FINAL", savedMeeting.getStatus().toString());
			assertEquals(1, savedMeeting.getPlaces().stream().filter(x -> x.getChosen() == true).count());

		} catch (RecordNotFoundException e) {
			// check expected exception
			if (!exMessage.equals("")) {
				assertEquals(e.getMessage(), exMessage);

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
	public void testGet(String id, boolean meetingExist, boolean exception) {

		// Given
		if (meetingExist)
			id = meetingId;

		// When
		try {
			Meeting retrieved = meetingService.get(id);
			// Then
			assertNotNull(retrieved);
		} catch (RecordNotFoundException e) {
			// check expected exception
			if (exception) {
				assertEquals(e.getMessage(), "The specified record does not exist");

			} else
				fail("Unexpected exception: " + e.getMessage());
		}
	}

}
