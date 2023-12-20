package com.makan.makangowhere.controllers;

import com.makan.makangowhere.models.CreateMeetingRequestModel;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.services.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class MakanRestController {
    private final MeetingService meetingService;

    // public MakanRestController(MeetingService meetingService) {
    // this.meetingService = meetingService;
    // }

    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Meeting> createMeeting(@RequestBody CreateMeetingRequestModel input) {
        Meeting meeting = new Meeting(input.getName(), input.getCreatedBy());
        Meeting savedProduct = meetingService.save(meeting);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

}
