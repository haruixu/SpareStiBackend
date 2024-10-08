package org.ntnu.idi.idatt2106.sparesti.sparestibackend.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter chain for JWT token that is executed for every request
 *
 * @author Harry L.X and Lars M.L.N
 * @since 17.4.24
 */
@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The JWT filter
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain Filter chain that executes after JWT filter
     * @throws IOException If token is invalid
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws IOException {
        try {
            logger.info("Checking 'Authorization' header");
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            logger.info("Extracting token");
            jwt = authHeader.substring(7);
            logger.info("Extracting username from token");
            username = jwtService.extractUsername(jwt);
            // Checks if user is not null and that it has not already been authenticated - else
            // there is
            // no point in re-authenticating
            logger.info(
                    "Checking that username is not null and that it hasn't been authenticated yet");
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.info("Finding user");
                // Retrieve user details from database for validation
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // If token is valid, update security context
                logger.info("Checking token validity");
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Set authentication context to current user");
                }
            }
            logger.info("Continue filter chain");
            filterChain.doFilter(request, response);
        } catch (JwtException | ServletException e) {
            logger.error("Caught exception in filter: {}", e.getMessage());
            String responseMsg = "Invalid or expired JWT token";
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(responseMsg);
        }
    }
}
