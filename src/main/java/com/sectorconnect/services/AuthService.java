package com.sectorconnect.services;

import com.sectorconnect.dto.AuthRequestDto;
import com.sectorconnect.dto.AuthResponseDto;
import com.sectorconnect.entities.User;
import com.sectorconnect.repositories.UserRepository;
import com.sectorconnect.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public void addJwtCookie(HttpServletResponse response, String token) {
        response.addCookie(createJwtCookie(token, 900));
    }

    public void clearJwtCookie(HttpServletResponse response) {
        response.addCookie(createJwtCookie("", 0));
    }

    public AuthResponseDto authenticate(AuthRequestDto request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new AuthResponseDto(jwtUtil.generateToken(user), user.getId());
    }

    public Optional<Long> extractUserIdFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            log.warn("No cookies found in request");
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .flatMap(token -> {
                    try {
                        return Optional.of(jwtUtil.extractUserId(token));
                    } catch (Exception e) {
                        log.warn("Invalid JWT token: {}", e.getMessage());
                        return Optional.empty();
                    }
                });
    }

    private Cookie createJwtCookie(String token, int maxAge) {
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(maxAge);
        jwtCookie.setAttribute("SameSite", "Strict");
        return jwtCookie;
    }
}
