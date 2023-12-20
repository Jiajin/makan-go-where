package com.makan.makangowhere.services;

import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

//    public MeetingService(MeetingRepository meetingRepository) {
//        this.meetingRepository = meetingRepository;
//    }

    public Meeting save(Meeting meeting) {
        Meeting savedMeeting = meetingRepository.save(meeting);
        return savedMeeting;
    }

}
