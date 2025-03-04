package com.sectorconnect.controllers;

import com.sectorconnect.dto.AuthRequestDto;
import com.sectorconnect.dto.AuthResponseDto;
import com.sectorconnect.dto.RegisterUserDto;
import com.sectorconnect.services.AuthService;
import com.sectorconnect.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        try {
            userService.registerUser(registerUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto request, HttpServletResponse response) {
        try {
            AuthResponseDto authResponse = authService.authenticate(request);
            authService.addJwtCookie(response, authResponse.getToken());
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.clearJwtCookie(response);
        return ResponseEntity.ok().build();
    }
}
