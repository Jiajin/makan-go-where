package com.makan.makangowhere.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.makan.errorMessages;
import com.makan.makangowhere.exceptions.RecordNotFoundException;
import com.makan.makangowhere.models.AcceptInviteRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.MeetingRepository;
import com.makan.makangowhere.repository.PersonRepository;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final MeetingRepository meetingRepository;

    public Person save(Person person) {
        Person savedPerson = personRepository.save(person);
        return savedPerson;
    }

    public Person savePersonMeeting(AcceptInviteRequest input) {

        Person person = new Person(input.getName(), input.getEmail());

        // Check if meeting exists
        Optional<Meeting> meetingOptional = meetingRepository.findById(input.getMeetingId());
        if (!meetingOptional.isPresent()) {
            throw new RecordNotFoundException(errorMessages.MeetingNotFound);
        }

        // update meeting list
        List<String> meetingList = new ArrayList<String>();
        meetingList.add(input.getMeetingId());
        person.setMeetingCsvList(meetingList);

        Person savedPerson = personRepository.save(person);

        return savedPerson;
    }

    public Person get(String email) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        if (!personOptional.isPresent()) {
            throw new RecordNotFoundException(errorMessages.PersonNotFound);
        }
        Person person = personOptional.get();

        // Populate Meeting entities
        List<String> meetingList = person.getMeetingCsvList();
        if (meetingList != null && meetingList.size() > 0) {
            person.setMeetingList(meetingRepository.findAllById(meetingList));
        }

        return person;
    }

}
