package com.yashvaria.notionflow.config;

import com.yashvaria.notionflow.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Constructor Injection
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check if the header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no token, smoothly pass the request down to the next filter in line
            // (unsecured endpoints like login/signup will pass, secured endpoints will be blocked later by the gateway)
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract token out of the "Bearer <TOKEN>" string (Index 7 skips the word and space)
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 3. If there is an email and the user is NOT already authenticated for this request thread yet
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Look up the full user credentials from Postgres using our UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. Validate the cryptographic signature and expiration of the token string
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Build a standard internal Spring Security Authentication ticket
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials are null because the token is already verified
                        userDetails.getAuthorities()
                );

                // Build tracking metadata about the request location/session details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. STUFF IT INTO THE CONTEXT!
                // This informs Spring Security that this request thread is officially trusted and fully signed in.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass control downstream to the final processing step
        filterChain.doFilter(request, response);
    }
}
