package net.expenses.recorder.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.exception.BadCredentialException;
import net.expenses.recorder.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Kazi Tanvir Azad
 */
@Component
@PropertySource(value = {"classpath:security-config-${spring.profiles.active}.properties"})
public class JWTManager implements CommonConstants {

    @Value("${SECRET.KEY}")
    private String SECRET_KEY;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String getTokenFromHttpRequest(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(JWT_BEARER)) {
            throw new BadCredentialException("Missing authorization bearer token");
        }
        String[] splitAuthorizationHeader = authorizationHeader.split(SINGLE_WHITESPACE);
        if (splitAuthorizationHeader.length > 1 && StringUtils.hasText(splitAuthorizationHeader[1])) {
            return splitAuthorizationHeader[1];
        }
        throw new BadCredentialException("Unauthorised token");
    }

    public boolean isTokenExpired(String token) {
        Date expiryDate = getClaim(token, Claims::getExpiration);
        return expiryDate.before(Date.from(Instant.now()));
    }

    public boolean isTokenValid(String token, User user) {
        return !isTokenExpired(token) && user.getEmail().equals(getSubject(token));
    }

    public String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(Map<String, Object> claims, User user) {

        return Jwts.builder()
                .claims().add(claims)
                .subject(user.getEmail())
                .issuer("dailyexpensesrecorder")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .and()
                .signWith(secretKey())
                .compact();
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }
}
