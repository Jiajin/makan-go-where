package com.makan.makangowhere.services;

import org.springframework.stereotype.Service;

import com.makan.makangowhere.models.Place;
import com.makan.makangowhere.repository.PlaceRepository;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place save(Place place) {
        Place savedPlace = placeRepository.save(place);
        return savedPlace;
    }
}
