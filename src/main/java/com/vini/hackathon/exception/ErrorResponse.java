package com.vini.hackathon.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String httpStatus;

    private String mensagem;

}

