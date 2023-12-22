package com.makan.makangowhere.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetMeetingRequestModel {
    @NotBlank
    private String id;
}
