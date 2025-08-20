package com.vini.hackathon.database.azure.repository;

import com.vini.hackathon.database.azure.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
