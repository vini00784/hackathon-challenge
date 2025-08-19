package com.vini.hackathon.dto.response.listagem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ListagemEspecificaSimulacoesResponse {

    private LocalDate dataReferencia;

    private List<SimulacaoDTO> simulacoes;

}
