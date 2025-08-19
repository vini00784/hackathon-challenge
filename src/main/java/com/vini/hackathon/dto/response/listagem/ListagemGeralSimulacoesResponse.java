package com.vini.hackathon.dto.response.listagem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ListagemGeralSimulacoesResponse {

    private int pagina;

    private int qtdRegistros;

    private int qtdRegistrosPagina;

    private List<RegistroDTO> registros;

}
