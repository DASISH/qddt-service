package no.nsd.qddt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * @author Dag Østgulen Heradstveit
 */
@Configuration
public class CORSConfiguration {

    @Bean
    public static FilterRegistrationBean corsFilter(@Value("${api.origin:*}") String origin) {
        return new FilterRegistrationBean(new Filter() {

            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                    throws IOException, ServletException {

                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) res;
                String method = request.getMethod();

                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
                response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");

                if ("OPTIONS".equals(method)) {
                    response.setStatus(HttpStatus.OK.value());
                }

                else {
                    chain.doFilter(req, res);
                }
            }

            public void init(FilterConfig filterConfig) {
            }

            public void destroy() {
            }

        });
    }
}
