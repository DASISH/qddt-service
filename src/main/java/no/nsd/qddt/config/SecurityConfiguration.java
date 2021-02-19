//package no.nsd.qddt.config;
//
//import no.nsd.qddt.security.MyUserDetailsServiceImpl;
//import no.nsd.qddt.security.jwt.JwtAuthenticationEntryPoint;
//import no.nsd.qddt.security.jwt.JwtAuthenticationTokenFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.Filter;
//
//import static org.springframework.security.config.Elements.AUTHENTICATION_MANAGER;
//
//
///**
// * @author Dag Østgulen Heradstveit
// */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//    prePostEnabled = true,
//    securedEnabled = true,
//    jsr250Enabled = true)
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    private final JwtAuthenticationEntryPoint unauthorizedHandler;
//
//    private final MyUserDetailsServiceImpl userDetailsService;
//
//    public SecurityConfiguration(JwtAuthenticationEntryPoint unauthorizedHandler, MyUserDetailsServiceImpl userDetailsService) {
//        this.unauthorizedHandler = unauthorizedHandler;
//        this.userDetailsService = userDetailsService;
//    }
//
//
//
////    @Autowired
////    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////            .userDetailsService(userDetailsService)
////            .passwordEncoder(passwordEncoderBean());
////    }
//
//    @Bean(AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public static  Filter authenticationTokenFilterBean() {
//        return new JwtAuthenticationTokenFilter();
//    }
//
//
//
//    /**
//     * Default {@link PasswordEncoder} using the newer and
//     * generally better BCrypt password algorithm.
//     * @return application wide password encoder.
//     */
//    @Bean
//    public static PasswordEncoder passwordEncoderBean() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoderBean());
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//            .authorizeRequests()
//            .antMatchers(HttpMethod.GET,"/preview/**").permitAll()
//            .antMatchers(HttpMethod.GET,"/othermaterial/files/**").permitAll()
//            .antMatchers("/auth/signin").permitAll()
//            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//            .anyRequest().authenticated().and()
//            .cors();
////            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
////            .antMatchers(HttpMethod.GET,"/preview/**").permitAll()
////            .antMatchers(HttpMethod.GET,"/othermaterial/files/**").permitAll()
////            .antMatchers(HttpMethod.DELETE, "/user/*").hasRole("ADMIN")
////            .antMatchers(HttpMethod.POST,"/user/*" ).access("hasAuthority('ROLE_ADMIN') or hasPermission('OWNER')")
////            .antMatchers(HttpMethod.GET, "/user/page/search/*" ).hasRole("ADMIN")
////            .antMatchers(HttpMethod.PATCH,"/user/resetpassword" ).access("hasAuthority('ROLE_ADMIN') or hasPermission('USER')")
////            .anyRequest().authenticated().and()
////            .csrf().disable()
////            .cors();
//
//        http
//            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
//
//        http
//            .headers().cacheControl();
//    }
//
//
////    @Override
////    public final void configure(final WebSecurity web) throws Exception {
////        super.configure(web);
////        web.httpFirewall(new AnnotatingHttpFirewall()); // Set the custom firewall.
////    }
//}
