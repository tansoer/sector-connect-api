package com.sectorconnect.controllers;

import com.sectorconnect.dto.UpdateUserDto;
import com.sectorconnect.services.AuthService;
import com.sectorconnect.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<UpdateUserDto> getLoggedInUser(HttpServletRequest request) {
        return authService.extractUserIdFromRequest(request)
                .map(userService::getUserById)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto, HttpServletRequest request) {
        return authService.extractUserIdFromRequest(request)
                .map(id -> ResponseEntity.ok(userService.updateUser(id, updateUserDto)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
