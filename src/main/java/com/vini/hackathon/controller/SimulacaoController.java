package com.vini.hackathon.controller;

import com.vini.hackathon.controller.definition.SimulacaoControllerDef;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.ListagemSimulacaoPorDataEProdRequest;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.listagem.ListagemEspecificaSimulacoesResponse;
import com.vini.hackathon.dto.response.listagem.ListagemGeralSimulacoesResponse;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import com.vini.hackathon.service.SimulacaoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class SimulacaoController implements SimulacaoControllerDef {

    @Autowired
    private SimulacaoService simulacaoService;

    @PostMapping( value = "/realizar-simulacao", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ControllerResponse<SolicitacaoSimulacaoResponse>> realizarSimulacao(@RequestBody SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest) {
        return ResponseEntity.ok(simulacaoService.solicitarSimulacaoCredito(solicitacaoSimulacaoRequest));
    }

    @GetMapping( value = "/simulacoes/pagina/{pagina}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ControllerResponse<ListagemGeralSimulacoesResponse>>  listarSimulacoes(@PathVariable("pagina") String pagina) {
        return ResponseEntity.ok(simulacaoService.listarSimulacoes(pagina));
    }

    @PostMapping( value = "/simulacoes-data-produto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ControllerResponse<ListagemEspecificaSimulacoesResponse>> listarSimulacoesPorDataEProduto(@RequestBody ListagemSimulacaoPorDataEProdRequest listagemSimulacaoPorDataEProdRequest) {
        return ResponseEntity.ok(simulacaoService.listarSimulacoesPorDataEProduto(listagemSimulacaoPorDataEProdRequest));
    }

}
