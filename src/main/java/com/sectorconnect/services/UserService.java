package com.sectorconnect.services;

import com.sectorconnect.dto.RegisterUserDto;
import com.sectorconnect.dto.UpdateUserDto;
import com.sectorconnect.entities.Sector;
import com.sectorconnect.entities.User;
import com.sectorconnect.mappers.UserMapper;
import com.sectorconnect.repositories.SectorRepository;
import com.sectorconnect.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SectorRepository sectorRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = userMapper.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userRepository.save(user);
    }

    public UpdateUserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public UpdateUserDto updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setName(updateUserDto.getName());
        user.setAgreedToTerms(updateUserDto.isAgreedToTerms());

        user.getSectors().clear();
        Set<Sector> selectedSectors = new HashSet<>(sectorRepository.findAllById(updateUserDto.getSectorIds()));
        user.setSectors(selectedSectors);

        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
