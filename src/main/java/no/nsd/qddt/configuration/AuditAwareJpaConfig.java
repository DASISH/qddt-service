package no.nsd.qddt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Stig Norland
 */
@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableJpaAuditing(auditorAwareRef = "customAuditProvider")
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class AuditAwareJpaConfig {

    @Bean
    public AuditAwareImpl customAuditProvider() {
        return new AuditAwareImpl();
    }
}
