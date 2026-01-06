package expensive.Expensive;

import expensive.Expensive.dto.AuthRequest;
import expensive.Expensive.dto.AuthResponse;
import expensive.Expensive.dto.InvestmentDto;
import expensive.Expensive.dto.ConsumptionDto;
import expensive.Expensive.entity.User;
import expensive.Expensive.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
public class ApiTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  private String getRootUrl() {
    return "http://localhost:" + port;
  }

  @Test
  public void testCompleteFlow() {
    // 1. Register a user
    User user = new User();
    user.setEmail("test@example.com");
    user.setPhone("1234567890");
    user.setPassword("password123");
    user.setName("Test User");

    ResponseEntity<String> registerResponse = restTemplate.postForEntity(getRootUrl() + "/api/auth/register", user,
        String.class);
    assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
    assertTrue(registerResponse.getBody().contains("registered successfully"));

    // 2. Login to get JWT token
    AuthRequest authRequest = new AuthRequest();
    authRequest.setEmail("test@example.com");
    authRequest.setPassword("password123");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);
    ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(getRootUrl() + "/api/auth/login", entity,
        AuthResponse.class);

    assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
    assertNotNull(loginResponse.getBody().getToken());
    String token = loginResponse.getBody().getToken();

    // 3. Use JWT token to create an investment
    InvestmentDto investmentDto = new InvestmentDto();
    investmentDto.setTitle("Stock Investment");
    investmentDto.setAmount(new BigInteger("100000"));
    investmentDto.setDate(LocalDateTime.now());

    headers.setBearerAuth(token);
    HttpEntity<InvestmentDto> investmentEntity = new HttpEntity<>(investmentDto, headers);
    ResponseEntity<expensive.Expensive.entity.Investment> investmentResponse = restTemplate.postForEntity(
        getRootUrl() + "/api/investments", investmentEntity, expensive.Expensive.entity.Investment.class);

    assertEquals(HttpStatus.OK, investmentResponse.getStatusCode());
    assertNotNull(investmentResponse.getBody().getId());
    assertEquals("Stock Investment", investmentResponse.getBody().getTitle());

    // 4. Use JWT token to create a consumption
    ConsumptionDto consumptionDto = new ConsumptionDto();
    consumptionDto.setTitle("Grocery Shopping");
    consumptionDto.setAmount(new BigInteger("500000"));
    consumptionDto.setDate(LocalDateTime.now());

    HttpEntity<ConsumptionDto> consumptionEntity = new HttpEntity<>(consumptionDto, headers);
    ResponseEntity<expensive.Expensive.entity.Consumption> consumptionResponse = restTemplate.postForEntity(
        getRootUrl() + "/api/consumptions", consumptionEntity, expensive.Expensive.entity.Consumption.class);

    assertEquals(HttpStatus.OK, consumptionResponse.getStatusCode());
    assertNotNull(consumptionResponse.getBody().getId());
    assertEquals("Grocery Shopping", consumptionResponse.getBody().getTitle());

    // 5. Get investments with JWT token
    ResponseEntity<expensive.Expensive.entity.Investment[]> investmentsResponse = restTemplate.exchange(
        getRootUrl() + "/api/investments", HttpMethod.GET,
        new HttpEntity<>(headers), expensive.Expensive.entity.Investment[].class);

    assertEquals(HttpStatus.OK, investmentsResponse.getStatusCode());
    assertTrue(investmentsResponse.getBody().length > 0);

    // 6. Get consumptions with JWT token
    ResponseEntity<expensive.Expensive.entity.Consumption[]> consumptionsResponse = restTemplate.exchange(
        getRootUrl() + "/api/consumptions", HttpMethod.GET,
        new HttpEntity<>(headers), expensive.Expensive.entity.Consumption[].class);

    assertEquals(HttpStatus.OK, consumptionsResponse.getStatusCode());
    assertTrue(consumptionsResponse.getBody().length > 0);
  }
}