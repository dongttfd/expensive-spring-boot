package expensive.Expensive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class ConsumptionDto {
  private String id;

  private String userId; // This will be set by the system from the JWT token

  @NotBlank(message = "Title is required")
  private String title;

  @NotNull(message = "Amount is required")
  private BigInteger amount;

  @NotNull(message = "Date is required")
  private LocalDateTime date;

  // Constructors
  public ConsumptionDto() {
  }

  public ConsumptionDto(String userId, String title, BigInteger amount, LocalDateTime date) {
    this.userId = userId;
    this.title = title;
    this.amount = amount;
    this.date = date;
  }

  // Getters and Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BigInteger getAmount() {
    return amount;
  }

  public void setAmount(BigInteger amount) {
    this.amount = amount;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }
}