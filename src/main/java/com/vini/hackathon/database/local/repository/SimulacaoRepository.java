package com.vini.hackathon.database.local.repository;

import com.vini.hackathon.database.local.entity.Simulacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacaoRepository extends JpaRepository<Simulacao, Integer> {

    @Override
    @NonNull
    Page<Simulacao> findAll( @NonNull Pageable pageable);

}
