package no.nsd.qddt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Stig Norland
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "customAuditProvider")
public class AuditAwareJpaConfig {

    @Bean
    public AuditAwareImpl customAuditProvider() {
        return new AuditAwareImpl();
    }
}
