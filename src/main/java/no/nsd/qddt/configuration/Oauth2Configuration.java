package no.nsd.qddt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * @author Dag Østgulen Heradstveit
 */
@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class Oauth2Configuration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
//        endpoints.authenticationManager(authentication ->
//                authenticationManager.getOrBuild().authenticate(authentication));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_USER")
                .scopes("write")
                .redirectUris("http://localhost:3000")
//                .resourceIds("client")
                .secret("password");
    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_USER')")
//                .checkTokenAccess("hasAuthority('ROLE_USER')");
//    }

}