package expensive.Expensive.repository;

import expensive.Expensive.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, String> {
  List<Consumption> findByUserId(String userId);

  List<Consumption> findByUserIdOrderByDateDesc(String userId);

  List<Consumption> findByUserIdAndDateBetween(String userId, java.time.LocalDateTime startDate,
      java.time.LocalDateTime endDate);
}