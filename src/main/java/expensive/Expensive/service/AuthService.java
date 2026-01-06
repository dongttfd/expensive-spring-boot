package expensive.Expensive.service;

import expensive.Expensive.dto.AuthRequest;
import expensive.Expensive.dto.AuthResponse;
import expensive.Expensive.entity.User;
import expensive.Expensive.repository.UserRepository;
import expensive.Expensive.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public AuthResponse login(AuthRequest authRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequest.getEmail(),
              authRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Get user to include user ID in token
      User user = userRepository.findByEmail(authRequest.getEmail())
          .orElseThrow(() -> new RuntimeException("User not found"));
      String token = jwtUtil.generateToken(authRequest.getEmail(), user.getId());

      return new AuthResponse(token, "Login successful");
    } catch (Exception e) {
      throw new RuntimeException("Invalid credentials");
    }
  }

  public String register(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    if (userRepository.existsByPhone(user.getPhone())) {
      throw new RuntimeException("Phone already exists");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

    return "User registered successfully";
  }
}