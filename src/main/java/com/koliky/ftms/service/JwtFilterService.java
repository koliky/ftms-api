package com.koliky.ftms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Service;

@Service
public class JwtFilterService {

    public Claims checkToken(HttpServletRequest request) throws ServletException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            final Claims claims = Jwts.parser().setSigningKey("pacific03srv")
                    .parseClaimsJws(token).getBody();
            return claims;
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }
    }
}