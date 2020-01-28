package com.khal.intern_survey.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.khal.intern_survey.dao")
@PropertySource("classpath:application.properties")
@EntityScan(basePackages={ "com.khal.intern_survey.entity" })
public class JPAPersistenceConfig {

}
