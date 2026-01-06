package expensive.Expensive.repository;

import expensive.Expensive.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, String> {
  Optional<UserDevice> findByDeviceIdAndUserId(String deviceId, String userId);

  Optional<UserDevice> findByAccessToken(String accessToken);

  void deleteByUserId(String userId);
}