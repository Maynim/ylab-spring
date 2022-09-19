package com.edu.ulab.app.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorWebResponse {
    private String errorMessage;
}
