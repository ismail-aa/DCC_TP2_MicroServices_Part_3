package org.example.securityservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class API {

    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private UserDetailsService userDetailsService;

    public API(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,  JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    Map<String,String> login(String username, String password) {

        Map<String,String> ID_token = new HashMap<>();
        Instant instant = Instant.now();

        // Verifier l'authentification
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username,password)
        );

        // Get scope
        String scope = authenticate.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(" "));

        // Creation des IDs tokens
        // 1) Access token
        JwtClaimsSet jwtClaimsSet_accessToken = JwtClaimsSet.builder()
                .subject(authenticate.getName())
                .issuer("Security_Service")
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.MINUTES))
                .claim("scope",scope)
                .build();

        String Access_token = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet_accessToken)).getTokenValue();


        // 2) Refresh token
        JwtClaimsSet jwtClaimsSet_refreshToken = JwtClaimsSet.builder()
                .subject(authenticate.getName())
                .issuer("Security_Service")
                .issuedAt(instant)
                .expiresAt(instant.plus(15, ChronoUnit.MINUTES))
                .build();

        String Refresh_token = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet_refreshToken)).getTokenValue();

        ID_token.put("Access_Token",Access_token);
        ID_token.put("Refresh_Token",Refresh_token);

        return ID_token;
    }


    @PostMapping("/refresh")
    public Map<String,String> refresh(String refresh_token) {
        Map<String,String> ID_token = new HashMap<>();

        if (refresh_token == null) {
            ID_token.put("Error","Refresh token is null" + HttpStatus.UNAUTHORIZED);
            return ID_token;
        }

        // Verifier signature
        Jwt decoded = jwtDecoder.decode(refresh_token);

        String username = decoded.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Instant instant = Instant.now();
        // Creation Access Token

        // Get scope
        String scope = userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(" "));

        // Creation des IDs tokens
        // 1) Access token
        JwtClaimsSet jwtClaimsSet_accessToken = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuer("Security_Service")
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.MINUTES))
                .claim("scope",scope)
                .build();

        String Access_token = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet_accessToken)).getTokenValue();

        ID_token.put("Access_Token",Access_token);
        ID_token.put("Refresh_Token",refresh_token);

        return ID_token;
    }
}