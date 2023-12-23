package com.makan.makangowhere.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinalizeMeetingRequest {
    @NotBlank
    private String meetingId;
    @NotBlank
    private String createdBy;

}
