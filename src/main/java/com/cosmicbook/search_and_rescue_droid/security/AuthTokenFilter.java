package com.cosmicbook.search_and_rescue_droid.security;

import com.cosmicbook.search_and_rescue_droid.repository.UserRepository;
import com.cosmicbook.search_and_rescue_droid.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    // We no longer need UserDetailsServiceImpl for the primary lookup.
    // @Autowired
    // private UserDetailsServiceImpl userDetailsService;

    // --- CHANGE 1: Inject UserRepository ---
    // We need this to find the user by their ID from the database.
    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            // First, validate the token. This also checks the signature.
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // --- CHANGE 2: Find user by ID, not username ---
                // The 'getUsernameFromJwtToken' method actually returns the user ID (from the 'sub' claim).
                String userId = jwtUtils.getUsernameFromJwtToken(jwt);

                // Find the user in the repository using their actual ID.
                UserDetails userDetails = userRepository.findById(userId)
                        .map(UserDetailsImpl::build) // Convert the User entity to a UserDetails object
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));

                // --- END OF CHANGES ---

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // It's better to use a proper logger in a real application
            System.err.println("Cannot set user authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}