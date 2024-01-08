package com.dobudobu.resto.Service;

import com.dobudobu.resto.Dto.AuthenticationRequestDto;
import com.dobudobu.resto.Dto.AuthenticationResponseDto;
import com.dobudobu.resto.Dto.RegisterDto;
import com.dobudobu.resto.Entity.Role;
import com.dobudobu.resto.Entity.Token;
import com.dobudobu.resto.Entity.TokenType;
import com.dobudobu.resto.Entity.User;
import com.dobudobu.resto.Repository.TokenRepository;
import com.dobudobu.resto.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
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
        User savedUser = userRepository.save(user);
        String jwttoken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwttoken);
        return AuthenticationResponseDto.builder()
                .token(jwttoken).
                build();
    }

    private void revokeAllUserTokens(User user){
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
            validUserToken.forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });
            tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(User user, String jwttoken) {
        var token = Token.builder()
                .user(user)
                .token(jwttoken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
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
        revokeAllUserTokens(user);
        saveUserToken(user, jwttoken);
        return AuthenticationResponseDto.builder()
                .token(jwttoken).
                build();
    }
}
