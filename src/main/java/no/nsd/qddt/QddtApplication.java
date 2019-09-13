package no.nsd.qddt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@SpringBootApplication
public class QddtApplication {

	@Value("${api.origin}")
	private String origin;

	public static void main(String[] args) {
		var ctx = SpringApplication.run(QddtApplication.class, args);

//		Iterable<ObjectMapper> objectMappers = ctx.getBeansOfType(ObjectMapper.class).values();
//
//		objectMappers.forEach(mapper -> mapper.registerModule(new Hibernate5Module()));
		System.out.println("QDDT ready");
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
//		return applicationBuilder.sources(QDDT.class);
//	}

//	@Bean
//	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//		var messageConverter = new MappingJackson2HttpMessageConverter();
//
//		messageConverter.setPrettyPrint(true);
//		messageConverter.setPrefixJson(false);
//
//		// Add JSON support for JDK.1.8 time API.
//		messageConverter.getObjectMapper().registerModule(new JavaTimeModule());
//		messageConverter.getObjectMapper().registerModule(new Hibernate5Module());
//
//		return messageConverter;
//	}
		@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(origin);
            }
        };
    }

}
