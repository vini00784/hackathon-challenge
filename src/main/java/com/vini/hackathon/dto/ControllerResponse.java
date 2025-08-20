package com.vini.hackathon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ControllerResponse<T> {

    private T response;

    public ControllerResponse<T> setResponse(T response) {
        this.response = response;
        return this;
    }

}
