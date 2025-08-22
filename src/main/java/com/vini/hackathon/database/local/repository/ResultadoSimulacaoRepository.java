package com.vini.hackathon.database.local.repository;

import com.vini.hackathon.database.local.entity.ResultadoSimulacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadoSimulacaoRepository extends JpaRepository<ResultadoSimulacao, Integer> {
}
