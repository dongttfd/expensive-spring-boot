package expensive.Expensive.service;

import expensive.Expensive.dto.ConsumptionDto;
import expensive.Expensive.entity.Consumption;
import expensive.Expensive.repository.ConsumptionRepository;
import expensive.Expensive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumptionService {

  @Autowired
  private ConsumptionRepository consumptionRepository;

  @Autowired
  private UserRepository userRepository;

  public Consumption createConsumption(ConsumptionDto consumptionDto) {
    // Validate user exists
    if (!userRepository.existsById(consumptionDto.getUserId())) {
      throw new RuntimeException("User not found");
    }

    Consumption consumption = new Consumption();
    consumption.setUserId(consumptionDto.getUserId());
    consumption.setTitle(consumptionDto.getTitle());
    consumption.setAmount(consumptionDto.getAmount());
    consumption.setDate(consumptionDto.getDate());

    return consumptionRepository.save(consumption);
  }

  public List<Consumption> getConsumptionsByUserId(String userId) {
    return consumptionRepository.findByUserIdOrderByDateDesc(userId);
  }

  public Consumption getConsumptionById(String id, String userId) {
    Consumption consumption = consumptionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Consumption not found"));

    if (!consumption.getUserId().equals(userId)) {
      throw new RuntimeException("Access denied: You can only access your own consumptions");
    }

    return consumption;
  }

  public Consumption updateConsumption(String id, ConsumptionDto consumptionDto, String userId) {
    Consumption consumption = consumptionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Consumption not found"));

    if (!consumption.getUserId().equals(userId)) {
      throw new RuntimeException("Access denied: You can only update your own consumptions");
    }

    consumption.setTitle(consumptionDto.getTitle());
    consumption.setAmount(consumptionDto.getAmount());
    consumption.setDate(consumptionDto.getDate());

    return consumptionRepository.save(consumption);
  }

  public void deleteConsumption(String id, String userId) {
    Consumption consumption = consumptionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Consumption not found"));

    if (!consumption.getUserId().equals(userId)) {
      throw new RuntimeException("Access denied: You can only delete your own consumptions");
    }

    consumptionRepository.deleteById(id);
  }
}