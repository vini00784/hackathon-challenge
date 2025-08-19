package com.vini.hackathon.dto.response.solicitacao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ResultadoSimulacaoDTO {

    private String tipo;

    private List<ParcelaDTO> parcelas;

}
