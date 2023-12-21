package com.makan.makangowhere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.makan.makangowhere.models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {

}
