package com.makan.makangowhere.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetMeetingRequest {
    @NotBlank
    private String id;
}
