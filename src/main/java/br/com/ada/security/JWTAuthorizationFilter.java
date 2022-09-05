package br.com.ada.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String SECRET = "battleGame";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER = "Authorization";

    private final CurrentUserDetailsService currentUserDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  CurrentUserDetailsService currentUserDetailsService) {
        super(authenticationManager);
        this.currentUserDetailsService = currentUserDetailsService;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String token = request.getHeader(HEADER);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request, token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request, String token) {

        if (token == null) // double ckeck
            return null;

        String userName = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(
                TOKEN_PREFIX, "")).getBody().getSubject();

        UserDetails userDetails = currentUserDetailsService.loadUserByUsername(userName);
        return userName != null ? new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities())
                : null;

    }

}
