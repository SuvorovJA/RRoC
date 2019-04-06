package ru.sua.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sua.security.TokenProvider;
import ru.sua.security.model.AuthToken;
import ru.sua.security.model.LoginUser;

/**
 * API to generate token
 * URL: http://localhost:8080/token
 * Method: POST
 * Payload: { "username": "ro", "password": "ro-password" }
 * <p>
 * if use BCrypt - look encoded passwords in log
 */

@RestController
@RequestMapping("/token")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
        log.info("TOKEN CREATION FOR: {}", loginUser.toString());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

}
