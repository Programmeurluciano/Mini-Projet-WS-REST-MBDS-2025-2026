package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.dto.*;
import com.miniprojet.wsrest.model.User;
import com.miniprojet.wsrest.repository.UserRepository;
import com.miniprojet.wsrest.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already used");
        }

        User user = new User();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);

        userRepository.save(user);

        emailVerificationService.generateVerificationToken(user);

        return "User registered. Check your email to confirm account.";
    }

    public AuthenticationResponse login(AuthenticationRequest request, HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) throw new RuntimeException("Account not confirmed");

        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken.getToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); 
        response.addCookie(refreshCookie);

        return new AuthenticationResponse(accessToken);
    }

    public AuthenticationResponse refresh(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new RuntimeException("Refresh token missing");

        Optional<Cookie> refreshCookie = Optional.empty();
        for (Cookie c : cookies) {
            if (c.getName().equals("refreshToken")) {
                refreshCookie = Optional.of(c);
                break;
            }
        }

        if (refreshCookie.isEmpty()) throw new RuntimeException("Refresh token missing");

        String refreshTokenStr = refreshCookie.get().getValue();
        RefreshToken tokenEntity = refreshTokenService.findByToken(refreshTokenStr);

        if (refreshTokenService.isTokenExpired(tokenEntity)) {
            throw new RuntimeException("Refresh token expired");
        }

        String accessToken = jwtService.generateToken(tokenEntity.getUser().getEmail());

        return new AuthenticationResponse(accessToken);
    }

    public String confirmAccount(String token) {

        User user = emailVerificationService.verifyToken(token);
        userRepository.save(user);
        return "Account confirmed. You can now login.";
    }

    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("refreshToken")) {
                    c.setValue("");
                    c.setMaxAge(0);
                    c.setPath("/");
                    response.addCookie(c);
                }
            }
        }

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("refreshToken")) {
                    try {
                        RefreshToken token = refreshTokenService.findByToken(c.getValue());
                        refreshTokenService.deleteByUser(token.getUser());
                    } catch (Exception ignored) {}
                }
            }
        }

        return "Logged out successfully";
    }

}
