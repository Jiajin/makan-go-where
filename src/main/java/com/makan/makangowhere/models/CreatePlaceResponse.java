package com.makan.makangowhere.models;

import lombok.Data;

@Data
public class CreatePlaceResponse {
    private Place place;
    private String errorMessage;
}