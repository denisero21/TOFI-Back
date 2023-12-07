package com.example.tofi.user.service;

import com.example.tofi.common.exception.DomainEntityExistsException;
import com.example.tofi.common.persistance.domain.userservice.RegisterUserRequest;
import com.example.tofi.common.persistance.domain.userservice.Role;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.example.tofi.common.persistance.repository.RoleRepository;
import com.example.tofi.common.persistance.repository.UserRepository;
import com.example.tofi.common.util.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageUtil ms;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private RegisterUserRequest userDto;
    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        userDto = new RegisterUserRequest();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        user = new User();
        user.setEmail(userDto.getEmail());

        role = new Role();
        role.setName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
    }

    @Test
    public void testRegisterWhenUserIsRegisteredThenSaveUser() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        userService.register(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterWhenUserEmailExistsThenThrowException() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        try {
            userService.register(userDto);
        } catch (DomainEntityExistsException ex) {
            verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
        }
    }
}