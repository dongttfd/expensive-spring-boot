package expensive.Expensive.controller;

import expensive.Expensive.dto.AuthRequest;
import expensive.Expensive.dto.AuthResponse;
import expensive.Expensive.entity.User;
import expensive.Expensive.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
    try {
      AuthResponse response = authService.login(authRequest);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(401).body(new AuthResponse(null, "Invalid credentials"));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody User user) {
    try {
      String result = authService.register(user);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout() {
    // In a stateless JWT system, logout is typically handled on the client side
    // by removing the token from client storage
    return ResponseEntity.ok("Logged out successfully");
  }
}