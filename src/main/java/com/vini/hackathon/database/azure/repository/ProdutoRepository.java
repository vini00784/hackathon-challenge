package com.vini.hackathon.database.azure.repository;

import com.vini.hackathon.database.azure.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query("""
        SELECT p FROM Produto p\s
        WHERE p.nuMinimoMeses <= :prazo
          AND (p.nuMaximoMeses IS NULL OR p.nuMaximoMeses >= :prazo)
          AND p.vrMinimo <= :valor
          AND (p.vrMaximo IS NULL OR p.vrMaximo >= :valor)
   \s""")
    Produto buscarProduto(@Param("valor") double valor, @Param("prazo") int prazo);

}
