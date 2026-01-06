package expensive.Expensive.service;

import expensive.Expensive.dto.InvestmentDto;
import expensive.Expensive.entity.Investment;
import expensive.Expensive.repository.InvestmentRepository;
import expensive.Expensive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestmentService {

  @Autowired
  private InvestmentRepository investmentRepository;

  @Autowired
  private UserRepository userRepository;

  public Investment createInvestment(InvestmentDto investmentDto) {
    // Validate user exists
    if (!userRepository.existsById(investmentDto.getUserId())) {
      throw new RuntimeException("User not found");
    }

    Investment investment = new Investment();
    investment.setUserId(investmentDto.getUserId());
    investment.setTitle(investmentDto.getTitle());
    investment.setAmount(investmentDto.getAmount());
    investment.setDate(investmentDto.getDate());

    return investmentRepository.save(investment);
  }

  public List<Investment> getInvestmentsByUserId(String userId) {
    return investmentRepository.findByUserIdOrderByDateDesc(userId);
  }

  public Investment getInvestmentById(String id, String userId) {
    Investment investment = investmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Investment not found"));

    if (!investment.getUserId().equals(userId)) {
      throw new RuntimeException("Access denied: You can only access your own investments");
    }

    return investment;
  }

  public Investment updateInvestment(String id, InvestmentDto investmentDto, String userId) {
    Investment investment = investmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Investment not found"));

    if (!investment.getUserId().equals(userId)) {
      throw new RuntimeException("Access denied: You can only update your own investments");
    }

    investment.setTitle(investmentDto.getTitle());
    investment.setAmount(investmentDto.getAmount());
    investment.setDate(investmentDto.getDate());

    return investmentRepository.save(investment);
  }

  public void deleteInvestment(String id, String userId) {
    Investment investment = investmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Investment not found"));

    if (!investment.getUserId().equals(userId)) {
      throw new RuntimeException("Access denied: You can only delete your own investments");
    }

    investmentRepository.deleteById(id);
  }
}