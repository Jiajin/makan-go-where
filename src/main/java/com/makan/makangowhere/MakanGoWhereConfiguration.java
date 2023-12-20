package com.makan.makangowhere;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class MakanGoWhereConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        System.out.println("auditorAware loaded");
        return () -> Optional.of("defaultAuditor");
    }
}
