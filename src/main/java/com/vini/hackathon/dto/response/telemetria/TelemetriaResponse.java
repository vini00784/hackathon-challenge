package com.vini.hackathon.dto.response.telemetria;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TelemetriaResponse {

    private LocalDate dataReferencia;

    private List<EndpointDTO> listaEndpoints;

}
