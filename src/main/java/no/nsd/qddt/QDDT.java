package no.nsd.qddt;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;
import com.google.common.collect.Iterables;
import org.hibernate.collection.spi.PersistentCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;

//import org.springframework.boot.context.web.SpringBootServletInitializer;

//import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@SpringBootApplication
public class QDDT extends SpringBootServletInitializer {

    @Value("${api.origin}")
    private String origin;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(QDDT.class);
        Iterable<ObjectMapper> objectMappers = ctx.getBeansOfType(ObjectMapper.class)
            .values();

        objectMappers.forEach(mapper -> mapper.registerModule(new Hibernate5Module()));

        Squiggly.init(objectMappers, new RequestSquigglyContextProvider() {
            @Override
            public void serializeAsIncludedField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
                if (isFilteringEnabled()) {
                    Object value = writer.getMember().getValue(pojo);

                    if (value instanceof PersistentCollection) {
                        ((PersistentCollection) value).forceInitialization();
                    }
                }

                super.serializeAsIncludedField(pojo, jgen, provider, writer);
            }

            @Override
            protected String customizeFilter(String filter, HttpServletRequest request, Class beanClass) {

                if (filter != null && Page.class.isAssignableFrom(beanClass)) {
                    filter = "**,content[" + filter + "]";
                }

                return filter;
            }
        });


        ObjectMapper objectMapper = Iterables.getFirst(objectMappers, null);

        // Enable Squiggly for Jackson message converter
        if (objectMapper != null) {
            for (MappingJackson2HttpMessageConverter converter : ctx.getBeansOfType(MappingJackson2HttpMessageConverter.class).values()) {
                converter.setObjectMapper(objectMapper);
            }
        }
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
    public FilterRegistrationBean squigglyRequestFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new SquigglyRequestFilter());
        filter.setOrder(1);
        return filter;
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