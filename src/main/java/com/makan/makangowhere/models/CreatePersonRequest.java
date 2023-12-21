package com.makan.makangowhere.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePersonRequest {
    private String name;
    private String email;
}
