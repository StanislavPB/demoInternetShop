package org.demointernetshop.controllers.user;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.user.UserDto;
import org.demointernetshop.dto.user.UserRegistrationDto;
import org.demointernetshop.dto.user.UserUpdateDto;
import org.demointernetshop.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(UserRegistrationDto request) {
        return userService.createUser(request);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Integer userId, UserUpdateDto request) {
        return userService.updateUser(userId, request);
    }
}
