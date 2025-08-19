package com.vini.hackathon.controller;

import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AppController {

    @PostMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
   ResponseEntity<String> test(@RequestBody SolicitacaoSimulacaoRequest body) {
        log.info("Prazo: {}", body.getPrazo());
        return ResponseEntity.ok("Testando");
    }

}
