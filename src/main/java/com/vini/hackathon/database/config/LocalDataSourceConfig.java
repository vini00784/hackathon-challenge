package com.vini.hackathon.database.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class LocalDataSourceConfig {

    @Bean
    @ConfigurationProperties("local.datasource")
    public DataSourceProperties localDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "localDataSource")
    @ConfigurationProperties("local.datasource")
    public DataSource localDataSource() {
        return localDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "localEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactory(
            @Qualifier("localDataSource") DataSource localDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(localDataSource);
        em.setPackagesToScan("com.vini.hackathon.database.local"); // <-- pacote das entidades do H2
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "localTransactionManager")
    public PlatformTransactionManager localTransactionManager(
            @Qualifier("localEntityManagerFactory") LocalContainerEntityManagerFactoryBean localEntityManagerFactory) {
        assert localEntityManagerFactory.getObject() != null;
        return new JpaTransactionManager(localEntityManagerFactory.getObject());
    }

}
