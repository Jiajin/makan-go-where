package com.makan.makangowhere.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePlaceRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String createdBy;
    @NotBlank
    private String meetingId;
}
