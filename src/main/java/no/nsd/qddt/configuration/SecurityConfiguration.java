package no.nsd.qddt.configuration;

import no.nsd.qddt.domain.user.QDDTUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author Dag Østgulen Heradstveit
 */
@Configuration
@EnableWebSecurity
//@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disabled CSRF for now.
        // INFO: http://en.wikipedia.org/wiki/Cross-site_request_forgery
        http
                .csrf().disable();

        http.authorizeRequests()
                .antMatchers("/**").hasAnyRole("USER", "ADMIN");
    }

    /**
     * Default {@link org.springframework.security.crypto.password.PasswordEncoder} using the newer and
     * generally better BCrypt password algorithm.
     * @return application wide password encoder.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private QDDTUserDetailsService qddtUserDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(qddtUserDetailsService).passwordEncoder(passwordEncoder);
        }
    }
}
