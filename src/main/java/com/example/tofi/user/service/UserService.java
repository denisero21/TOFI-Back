package com.example.tofi.user.service;

import com.example.tofi.common.exception.DomainEntityExistsException;
import com.example.tofi.common.persistance.domain.userservice.RegisterUserRequest;
import com.example.tofi.common.persistance.domain.userservice.Role;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.example.tofi.common.persistance.domain.userservice.dto.ErrorCode;
import com.example.tofi.common.persistance.repository.RoleRepository;
import com.example.tofi.common.persistance.repository.UserRepository;
import com.example.tofi.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MessageUtil ms;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public void register(RegisterUserRequest userDto) {

        checkExistUserEmail(userDto.getEmail());

        User user = new User();
        BeanUtils.copyProperties(userDto, user, "password", "date");
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodedPassword);
        user.setDate(LocalDateTime.now());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById(1L).orElse(null));
        user.setRoles(roles);
        userRepository.save(user);
    }
    private void checkExistUserEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DomainEntityExistsException(format(ms.getMessage("user.Exists"), email), ErrorCode.EMAIL_EXISTS);
        }
    }

}
