package com.makan.makangowhere.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePlaceRequest {
    private String name;
    private String address;
    private String createdBy;
    private String meetingId;
}
