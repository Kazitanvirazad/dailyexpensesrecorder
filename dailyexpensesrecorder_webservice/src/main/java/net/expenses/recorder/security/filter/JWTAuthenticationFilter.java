package net.expenses.recorder.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.exception.BadCredentialException;
import net.expenses.recorder.security.JWTAuthenticationToken;
import net.expenses.recorder.security.JWTManager;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.validation.UserValidationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Kazi Tanvir Azad
 */
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTManager jwtManager;
    private final UserService userService;


    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     * @param filterChain {@link FilterChain}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtManager.getTokenFromHttpRequest(request);
            String email = jwtManager.getSubject(token);
            if (!UserValidationHelper.isValidEmail(email)) {
                throw new BadCredentialException("Unauthorised/Invalid Token.");
            }
            User user = userService.getUserByEmail(email);
            if (user == null) {
                throw new BadCredentialException("Unauthorised/Invalid Token.");
            }
            if (user.getLoggedOut()) {
                throw new BadCredentialException("Login required. User is already logged out.");
            }
            if (jwtManager.isTokenValid(token, user)) {
                Authentication authentication = new JWTAuthenticationToken(user, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                throw new BadCredentialException("Token Expired.");
            }
        } catch (BadCredentialException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(exception.getMessage());
        } catch (JwtException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorised/Invalid Token.");
        }
    }
}
