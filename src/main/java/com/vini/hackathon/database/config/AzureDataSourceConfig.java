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
public class AzureDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties azureDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "azureDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource azureDataSource() {
        return azureDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean azureEntityManagerFactory(
            @Qualifier("azureDataSource") DataSource azureDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(azureDataSource);
        em.setPackagesToScan("com.vini.hackathon.database.azure.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager azureTransactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean azureEntityManagerFactory) {
        assert azureEntityManagerFactory.getObject() != null;
        return new JpaTransactionManager(azureEntityManagerFactory.getObject());
    }
}
