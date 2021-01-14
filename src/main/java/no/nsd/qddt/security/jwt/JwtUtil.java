package no.nsd.qddt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.nsd.qddt.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Common helper methods to work with JWT
 */
@Component
public class JwtUtil implements Serializable {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_ID = "id";
    private static final String CLAIM_KEY_CREATED = "iat";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_EMAIL = "email";


    @Value("${auth.secret}")
    private String secret;

    @Value("${auth.expires}")
    private Long expiration;

    /**
     * Returns user id from given token
     * @param token JSON Web Token
     * @return user id
     */
    public UUID getUserIdFromToken(String token) {
        UUID id = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            id = UUID.fromString(claims.get(CLAIM_KEY_ID).toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    /**
     * Returns username from given token
     * @param token JSON Web Token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }


    /**
     * Returns creation date from given token
     * @param token JSON Web Token
     * @return creation date
     */
    public Date getCreationDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * Returns expiration date from given token
     * @param token JSON Web Token
     * @return expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * Generates JWT using userDetails
     * @param userDetails used to generate JWT
     * @return generated JWT
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        UserPrincipal jwtUser = (UserPrincipal) userDetails;
        claims.put(CLAIM_KEY_ID, jwtUser.getId());
        claims.put(CLAIM_KEY_EMAIL, jwtUser.getUser().getEmail());
        claims.put(CLAIM_KEY_USERNAME, jwtUser.getUsername());
        claims.put(CLAIM_KEY_ROLE, jwtUser.getAuthorities());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * Refreshes JWT
     * @param token old JWT
     * @return refreshed JWT
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * Checks token validity
     * @param token to check
     * @param userDetails to compare with
     * @return true if token valid else false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
//        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        if(username == null) {
            return false;
        } else {
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }
//        Better....
//        username.equals(user.getUsername())
//                && !isTokenExpired(token)
//                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | ClassCastException e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
