package expensive.Expensive.controller;

import expensive.Expensive.dto.InvestmentDto;
import expensive.Expensive.entity.Investment;
import expensive.Expensive.service.InvestmentService;
import expensive.Expensive.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin(origins = "*")
public class InvestmentController {

  @Autowired
  private InvestmentService investmentService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private expensive.Expensive.service.UserDetailsServiceImpl userDetailsService;

  // Extract user ID from JWT token
  private String getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // In a real implementation, you would typically store the user ID in the JWT
    // claims
    // and retrieve it in the JwtAuthenticationFilter, setting it as the principal
    // For now, we'll get the user ID from the database using the email
    expensive.Expensive.entity.User user = userDetailsService.loadUserByEmail(email);

    return user.getId();
  }

  @PostMapping
  public ResponseEntity<Investment> createInvestment(@Valid @RequestBody InvestmentDto investmentDto) {
    String userId = getCurrentUserId();
    investmentDto.setUserId(userId); // Set the user ID from the authenticated user
    Investment investment = investmentService.createInvestment(investmentDto);
    return ResponseEntity.ok(investment);
  }

  @GetMapping
  public ResponseEntity<List<Investment>> getInvestments() {
    String userId = getCurrentUserId();
    List<Investment> investments = investmentService.getInvestmentsByUserId(userId);
    return ResponseEntity.ok(investments);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Investment> getInvestment(@PathVariable String id) {
    String userId = getCurrentUserId();
    Investment investment = investmentService.getInvestmentById(id, userId);
    return ResponseEntity.ok(investment);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Investment> updateInvestment(@PathVariable String id,
      @Valid @RequestBody InvestmentDto investmentDto) {
    String userId = getCurrentUserId();
    Investment investment = investmentService.updateInvestment(id, investmentDto, userId);
    return ResponseEntity.ok(investment);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteInvestment(@PathVariable String id) {
    String userId = getCurrentUserId();
    investmentService.deleteInvestment(id, userId);
    return ResponseEntity.ok("Investment deleted successfully");
  }
}