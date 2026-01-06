package expensive.Expensive.controller;

import expensive.Expensive.dto.ConsumptionDto;
import expensive.Expensive.entity.Consumption;
import expensive.Expensive.service.ConsumptionService;
import expensive.Expensive.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumptions")
@CrossOrigin(origins = "*")
public class ConsumptionController {

  @Autowired
  private ConsumptionService consumptionService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private expensive.Expensive.service.UserDetailsServiceImpl userDetailsService;

  // Extract user ID from JWT token
  private String getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Get the user from the database to retrieve the user ID
    expensive.Expensive.entity.User user = userDetailsService.loadUserByEmail(email);

    return user.getId();
  }

  @PostMapping
  public ResponseEntity<Consumption> createConsumption(@Valid @RequestBody ConsumptionDto consumptionDto) {
    String userId = getCurrentUserId();
    consumptionDto.setUserId(userId); // Set the user ID from the authenticated user
    Consumption consumption = consumptionService.createConsumption(consumptionDto);
    return ResponseEntity.ok(consumption);
  }

  @GetMapping
  public ResponseEntity<List<Consumption>> getConsumptions() {
    String userId = getCurrentUserId();
    List<Consumption> consumptions = consumptionService.getConsumptionsByUserId(userId);
    return ResponseEntity.ok(consumptions);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Consumption> getConsumption(@PathVariable String id) {
    String userId = getCurrentUserId();
    Consumption consumption = consumptionService.getConsumptionById(id, userId);
    return ResponseEntity.ok(consumption);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Consumption> updateConsumption(@PathVariable String id,
      @Valid @RequestBody ConsumptionDto consumptionDto) {
    String userId = getCurrentUserId();
    Consumption consumption = consumptionService.updateConsumption(id, consumptionDto, userId);
    return ResponseEntity.ok(consumption);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteConsumption(@PathVariable String id) {
    String userId = getCurrentUserId();
    consumptionService.deleteConsumption(id, userId);
    return ResponseEntity.ok("Consumption deleted successfully");
  }
}