package dev.maxuz.vas3ksanta.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"dev.maxuz.vas3ksanta.db", "dev.maxuz.vas3ksanta.model"})
public class DatabaseConfig {
}
