package com.makan.makangowhere.services;

import org.springframework.stereotype.Service;

import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.repository.PersonRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Person save(Person person) {
        Person savedPerson = personRepository.save(person);
        return savedPerson;
    }
}
