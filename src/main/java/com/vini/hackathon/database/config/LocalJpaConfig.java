package com.vini.hackathon.database.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.vini.hackathon.database.local.repository", // Repositórios do H2
        entityManagerFactoryRef = "localEntityManagerFactory",
        transactionManagerRef = "localTransactionManager"
)
@EntityScan(basePackages = "com.vini.hackathon.database.local.entity") // Entidades do H2
public class LocalJpaConfig {



}
