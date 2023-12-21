package com.makan.makangowhere.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePersonRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
