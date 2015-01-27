package no.nsd.qddt.configuration;

import no.nsd.qddt.service.QDDTUserDetailsService;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Dag Østgulen Heradstveit
 */
@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disabled CSRF for now.
        // INFO: http://en.wikipedia.org/wiki/Cross-site_request_forgery
        http
                .csrf().disable();
//
//        http.authorizeRequests()
//                .antMatchers("/**").hasAnyRole("USER", "ADMIN");
    }

    /**
     * This bean sets up the servlet for the H2 database web console.
     * @return a servlet registration of the console.
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }

    /**
     * Create exceptions for the security filter.
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                        //Open up the swagger json stuff.
                .antMatchers("/console/*");
    }

    /**
     * Default {@link org.springframework.security.crypto.password.PasswordEncoder} using the newer and
     * generally better BCrypt password algorithm.
     * @return application wide password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    protected static class AUthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        QDDTUserDetailsService qddtUserDetailsService;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(this.qddtUserDetailsService).passwordEncoder(passwordEncoder);
        }
    }
}
