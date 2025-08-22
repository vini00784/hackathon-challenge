package com.vini.hackathon.controller;

import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.listagem.ListagemGeralSimulacoesResponse;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import com.vini.hackathon.exception.BusinessException;
import com.vini.hackathon.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping( value = "/realizar-simulacao", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ControllerResponse<SolicitacaoSimulacaoResponse>> realizarSimulacao(@RequestBody SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest) throws BusinessException {
        return ResponseEntity.ok(appService.solicitarSimulacaoCredito(solicitacaoSimulacaoRequest));
    }

    @GetMapping( value = "/simulacoes", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ControllerResponse<ListagemGeralSimulacoesResponse>> listarSimulacoes() throws BusinessException {
        return ResponseEntity.ok(appService.listarSimulacoes());
    }

}
