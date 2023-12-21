package com.makan.makangowhere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.makan.makangowhere.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

}
