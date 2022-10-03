package com.edu.ulab.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDto {
    private Long id;
    private String fullName;
    private String title;
    private String email;
    private int age;
}
