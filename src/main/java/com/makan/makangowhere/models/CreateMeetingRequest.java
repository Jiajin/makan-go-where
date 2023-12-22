package com.makan.makangowhere.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMeetingRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String createdBy;

}
