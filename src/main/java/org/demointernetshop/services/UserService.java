package org.demointernetshop.services;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.user.UserDto;
import org.demointernetshop.dto.user.UserRegistrationDto;
import org.demointernetshop.dto.user.UserUpdateDto;
import org.demointernetshop.entity.Role;
import org.demointernetshop.entity.User;
import org.demointernetshop.repository.RoleRepository;
import org.demointernetshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;

    public UserDto createUser(UserRegistrationDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        Role role = roleRepository.findByName("User")
                .orElseThrow(() -> new RuntimeException("Role 'User' not found in the database"));
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setEmail(request.getEmail());
        newUser.setRole(role);
        newUser.setPhone(request.getPhone());
        newUser.setCreateData(LocalDateTime.now());
        newUser.setLastVisitData(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        return UserDto.from(savedUser);
    }

    public UserDto updateUser(Integer userId, UserUpdateDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for user id: " + userId));

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return UserDto.from(user);
    }
}
