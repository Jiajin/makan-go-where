package com.makan.makangowhere.models;

import lombok.Data;

@Data
public class CreateMeetingResponse {
    private Meeting meeting;
    private String errorMessage;

}
