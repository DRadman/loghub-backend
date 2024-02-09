package net.decodex.loghub.backend.services;

import com.mailjet.client.errors.MailjetException;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.config.auth.JwtService;
import net.decodex.loghub.backend.config.auth.UserSecurity;
import net.decodex.loghub.backend.domain.dto.*;
import net.decodex.loghub.backend.domain.dto.requests.AuthenticationRequestDto;
import net.decodex.loghub.backend.domain.dto.requests.ForgotPasswordRequestDto;
import net.decodex.loghub.backend.domain.dto.requests.RefreshTokenRequestDto;
import net.decodex.loghub.backend.domain.dto.requests.RegisterUserRequestDto;
import net.decodex.loghub.backend.domain.mappers.UserMapper;
import net.decodex.loghub.backend.domain.models.User;
import net.decodex.loghub.backend.exceptions.specifications.AuthenticationException;
import net.decodex.loghub.backend.exceptions.specifications.RegistrationDisabledException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceAlreadyExistsException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.RoleRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final MailService mailService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.registration.enabled}")
    private boolean isRegistrationEnabled;


    public TokenResponseDto authenticate(AuthenticationRequestDto authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();
        UserSecurity securityUser = new UserSecurity(user);
        String token = jwtService.generateToken(securityUser, new HashMap<>());
        String refreshToken = jwtService.generateRefreshToken(securityUser, new HashMap<>());
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        return new TokenResponseDto(token, refreshToken, jwtService.getTokenDuration(), jwtService.getRefreshTokenDuration());
    }

    public TokenResponseDto refreshToken(RefreshTokenRequestDto dto) {
        String username = jwtService.extractUsername(dto.getRefreshToken());
        if (username == null) {
            throw new AuthenticationException("Invalid token subject");
        }

        Optional<User> opuser = userRepository.findByUsername(username);
        if (opuser.isEmpty()) {
            throw new ResourceNotFoundException("Username not found for client_id");
        }
        User user = opuser.get();
        UserSecurity securityUser = new UserSecurity(user);
        if (jwtService.isTokenValid(dto.getRefreshToken(), securityUser)) {
            String token = jwtService.generateToken(securityUser, new HashMap<>());
            String refreshToken = jwtService.generateRefreshToken(securityUser, new HashMap<>());
            return new TokenResponseDto(token, refreshToken, jwtService.getTokenDuration(), jwtService.getRefreshTokenDuration());
        } else {
            throw new AuthenticationException("Refresh Token Has Been Expired or is Invalid");
        }
    }

    public UserDto getLoggedUserInformation(String username) {
        Optional<User> opuser = userRepository.findByUsername(username);
        if (opuser.isEmpty()) {
            throw new ResourceNotFoundException("Invalid user authorization");
        }
        return userMapper.toDto(opuser.get());
    }

    public User getLoggedUser(String username) {
        Optional<User> opuser = userRepository.findByUsername(username);
        if (opuser.isEmpty()) {
            throw new ResourceNotFoundException("Invalid user authorization");
        }
        return opuser.get();
    }

    public UserDto registerNewUser(RegisterUserRequestDto dto) {
        if (!isRegistrationEnabled) {
            throw new RegistrationDisabledException("Registration is disabled by the server admin");
        } else {
            if(userRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new ResourceAlreadyExistsException("User", "username", dto.getUsername());
            }
            var user = userMapper.toEntity(dto);
            var role = roleRepository.findByName("Owner");
            if (role.isEmpty()) {
                throw new ResourceNotFoundException("Role", "name", "Owner");
            }
            user.setRole(role.get());
            user.setActivated(true);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            return userMapper.toDto(userRepository.save(user));
        }
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDto forgotPassword(ForgotPasswordRequestDto dto) {
        var user = userRepository.findByUsername(dto.getUsername());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", dto.getUsername());
        }

        try {
            mailService.sendForgotPassword(user.get());
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }

        return userMapper.toDto(user.get());
    }
}
