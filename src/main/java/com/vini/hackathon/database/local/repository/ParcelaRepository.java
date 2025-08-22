package com.vini.hackathon.database.local.repository;

import com.vini.hackathon.database.local.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Integer> {
}
