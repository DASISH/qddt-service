package no.nsd.qddt;


import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//import org.springframework.boot.context.web.SpringBootServletInitializer;

//import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author Dag Østgulen Heradstveit
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@SpringBootApplication
public class QDDT extends SpringBootServletInitializer {

    @Value("${api.origin}")
    private String origin;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(QDDT.class);
        System.out.println("QDDT ready");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(QDDT.class);

    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter =
                new MappingJackson2HttpMessageConverter();

        messageConverter.setPrettyPrint(true);
        messageConverter.setPrefixJson(false);

        // Add JSON support for JDK.1.8 time API.
        messageConverter.getObjectMapper().registerModule(new JavaTimeModule());
        messageConverter.getObjectMapper().registerModule(new Hibernate5Module());

        return messageConverter;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(origin);
            }
        };
    }
}