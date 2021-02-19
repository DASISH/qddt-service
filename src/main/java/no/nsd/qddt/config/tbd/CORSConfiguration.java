package no.nsd.qddt.config.tbd;

//import org.springframework.boot.context.embedded.FilterRegistrationBean;

//import org.springframework.boot.web.servlet.FilterRegistrationBean;

///**
// * @author Dag Østgulen Heradstveit
// */
//@Configuration
//public class CORSConfiguration {
//    @Bean
//    public CorsFilter corsFilter(@Value("${api.origin}") String origin) {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Arrays.asList(origin.split( "," ))   );
////        config.addAllowedOrigin( );
////        config.addAllowedHeader("*");
//        config.addAllowedMethod("OPTIONS");
//        config.addAllowedMethod("GET");
////        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("DELETE");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//
//}
