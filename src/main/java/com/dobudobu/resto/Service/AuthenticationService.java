package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.AuthenticationRequestDto;
import com.dobudobu.resto.Dto.AuthenticationResponseDto;
import com.dobudobu.resto.Dto.RegisterDto;
import com.dobudobu.resto.Entity.Role;
import com.dobudobu.resto.Entity.User;
import com.dobudobu.resto.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterDto registerDto) {

        User user = User.builder()
                .firstName(registerDto.getFirstname())
                .lastName(registerDto.getLastname())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Role.USER)
                .build();
        userRepository.save(user);
        String jwttoken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwttoken).
                build();
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDto.getEmail(),
                        authenticationRequestDto.getPassword()
                )
        );
        User user = userRepository.findByEmail(authenticationRequestDto.getEmail()).orElseThrow();
        String jwttoken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwttoken).
                build();
    }
}
