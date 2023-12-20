package com.makan.makangowhere.services;

import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.repository.MeetingRepository;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MeetingServiceTest {
	@Autowired
	private MeetingService meetingService;

	@MockBean
	private MeetingRepository meetingRepository;

	@Test
	public void save() {
		Meeting meeting = new Meeting("abc", "");
		Meeting expected = new Meeting("abc", "");
		Meeting actual = meetingService.save(meeting);

		assertEquals(expected, actual);
	}
}
