package com.vini.hackathon.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorWrapper {

    private ErrorResponse error;

}
