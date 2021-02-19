package no.nsd.qddt.config;

/**
 * @author Stig Norland
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({"no.nsd.qddt.security","no.nsd.qddt.config","no.nsd.qddt.domain"})
@EnableWebMvc
@EnableCaching
@EnableHypermediaSupport(type= EnableHypermediaSupport.HypermediaType.HAL)
@EnableJpaAuditing(auditorAwareRef = "customAuditProvider")
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class, basePackages={"no.nsd.qddt.domain"})
public class QddtConfig {

    @Value("${api.origin}")
    String origin;

    @Bean
    FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter()  {
        return new FilterRegistrationBean<>( new ForwardedHeaderFilter() );
    }

    @Bean
    FilterRegistrationBean<CacheControlFilter> cacheControlFilter()  {
        FilterRegistrationBean<CacheControlFilter> registration = new FilterRegistrationBean<>(new CacheControlFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins(origin.split("," )  );
//            }
//        };
//    }

    @Bean
    public AuditAwareImpl customAuditProvider() {
        return new AuditAwareImpl();
    }

}
