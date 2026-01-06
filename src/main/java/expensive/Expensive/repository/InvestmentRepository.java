package expensive.Expensive.repository;

import expensive.Expensive.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String> {
  List<Investment> findByUserId(String userId);

  List<Investment> findByUserIdOrderByDateDesc(String userId);

  List<Investment> findByUserIdAndDateBetween(String userId, java.time.LocalDateTime startDate,
      java.time.LocalDateTime endDate);
}