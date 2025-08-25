package com.vini.hackathon.database.local.repository;

import com.vini.hackathon.database.local.entity.Simulacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SimulacaoRepository extends JpaRepository<Simulacao, Integer> {

    @Override
    @NonNull
    Page<Simulacao> findAll( @NonNull Pageable pageable);

    @Query("SELECT s FROM Simulacao s WHERE s.codigoProduto = :codigoProduto AND s.createdAt = :dataReferencia")
    List<Simulacao> listarPorDataEProduto(@Param("codigoProduto") int codigoProduto, @Param("dataReferencia") LocalDate dataReferencia);

}
