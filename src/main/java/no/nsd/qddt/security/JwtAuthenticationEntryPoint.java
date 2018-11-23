package no.nsd.qddt.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
  
  private static final long serialVersionUID = -8162634993459603882L;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
      System.out.println("JwtAuthenticationEntryPoint");

      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
  }
  
}
