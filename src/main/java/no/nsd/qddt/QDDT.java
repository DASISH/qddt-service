package no.nsd.qddt;

import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import no.nsd.qddt.domain.Study;
import no.nsd.qddt.domain.Survey;
import no.nsd.qddt.domain.User;
import no.nsd.qddt.service.StudyService;
import no.nsd.qddt.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;

/**
 * @author Dag Østgulen Heradstveit
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@SpringBootApplication
public class QDDT extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(QDDT.class);
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
        messageConverter.getObjectMapper().registerModule(new JSR310Module());

        return messageConverter;
    }

    @Autowired
    private StudyService studyService;

    @Autowired
    private SurveyService surveyService;

    @PostConstruct
    public void init() {
        SampleApplicationData sampleApplicationData =
                new SampleApplicationData(studyService, surveyService);
    }


}