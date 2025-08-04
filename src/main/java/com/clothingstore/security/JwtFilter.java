//package com.clothingstore.security;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.clothingstore.model.User;
//import com.clothingstore.repository.UserRepository;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserRepository userRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            if (jwtUtil.validateToken(token)) {
//                String email = jwtUtil.extractEmail(token);
//                User user = userRepository.findByEmail(email).orElse(null);
//
//                if (user != null) {
//                    System.out.println("Authenticated: " + user.getEmail() + " Role: " + user.getRole());
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(
//                                    user,
//                                    null,
//                                    List.of(new SimpleGrantedAuthority(user.getRole().name()))
////                                    Collections.singleton(() -> "ROLE_" + user.getRole().name())
//                                    );
//
//                        
//
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}

package com.clothingstore.security;

import com.clothingstore.model.User;
import com.clothingstore.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Validate token
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);

                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    // Debugging logs
                    System.out.println("DEBUG: Authenticated Email: " + email);
                    System.out.println("DEBUG: User Role from DB: " + user.getRole());

                    // Set authorities based on role
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(authority)
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Debug authorities
                    System.out.println("DEBUG: Assigned Authorities: " + authentication.getAuthorities());
                } else {
                    System.out.println("DEBUG: User not found for email: " + email);
                }
            } else {
                System.out.println("DEBUG: Invalid JWT token");
            }
        } else {
            System.out.println("DEBUG: No Authorization header or Bearer token");
        }

        filterChain.doFilter(request, response);
    }
}

