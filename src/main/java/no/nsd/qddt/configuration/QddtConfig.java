package no.nsd.qddt.configuration;

/**
 * @author Stig Norland
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableCaching
@EnableScheduling
@EnableHypermediaSupport(type= EnableHypermediaSupport.HypermediaType.HAL)
public class QddtConfig {

    @Value("${api.origin}")
    String origin;

    @Bean
    FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter()  {
        return new FilterRegistrationBean( new ForwardedHeaderFilter() );
    }

    @Bean
    FilterRegistrationBean<OncePerRequestFilter> cacheControlFilter()  {
        FilterRegistrationBean registration = new FilterRegistrationBean(new CacheControlFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(origin.split("," )  );
            }
        };
    }


}
