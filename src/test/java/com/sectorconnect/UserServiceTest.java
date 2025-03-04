package com.sectorconnect;

import com.sectorconnect.dto.RegisterUserDto;
import com.sectorconnect.dto.UpdateUserDto;
import com.sectorconnect.entities.Sector;
import com.sectorconnect.entities.User;
import com.sectorconnect.mappers.UserMapper;
import com.sectorconnect.repositories.SectorRepository;
import com.sectorconnect.repositories.UserRepository;
import com.sectorconnect.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {
        // Given
        RegisterUserDto registerUserDto = new RegisterUserDto("johndoe", "password123", "John Doe");
        String hashedPassword = "$2a$10$hashedpassword123";

        User user = new User(null, "johndoe", hashedPassword, "John Doe", false, new HashSet<>(), LocalDateTime.now());

        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn(hashedPassword);
        when(userMapper.toEntity(registerUserDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        userService.registerUser(registerUserDto);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(registerUserDto.getPassword());
    }

    @Test
    void testRegisterUser_UsernameExists() {
        // Given
        RegisterUserDto registerUserDto = new RegisterUserDto("johndoe", "password123", "John Doe");

        when(userRepository.findByUsername(registerUserDto.getUsername())).thenReturn(Optional.of(new User()));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registerUserDto));
        Assertions.assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testUpdateUser() {
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "John Doe", true, Set.of(3L, 4L));
        Sector sector1 = new Sector(3L, "Food and Beverage", true, null, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Sector sector2 = new Sector(4L, "Furniture", true, null, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Set<Sector> selectedSectors = Set.of(sector1, sector2);

        User user = new User(1L, "johndoe", "$2a$10$hashedpassword123", "John Doe", true, new HashSet<>(), LocalDateTime.now());
        User updatedUser = new User(1L, "johndoe", "$2a$10$hashedpassword123", "John Doe", true, selectedSectors, LocalDateTime.now());

        UpdateUserDto updatedUserDto = new UpdateUserDto(1L, "John Doe", true, Set.of(3L, 4L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sectorRepository.findAllById(updateUserDto.getSectorIds())).thenReturn(List.of(sector1, sector2));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(updatedUserDto);

        // When
        UpdateUserDto result = userService.updateUser(1L, updateUserDto);

        // Then
        assertNotNull(result);
        Assertions.assertEquals("John Doe", result.getName());
        Assertions.assertEquals(2, result.getSectorIds().size());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "John Doe", true, Set.of(3L, 4L));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, updateUserDto));
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetUserById() {
        // Given
        User user = new User(1L, "johndoe", "$2a$10$hashedpassword123", "John Doe", true, new HashSet<>(), LocalDateTime.now());
        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "John Doe", true, Set.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(updateUserDto);

        // When
        UpdateUserDto result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        Assertions.assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(999L));
        Assertions.assertEquals("User not found", exception.getMessage());
    }
}
