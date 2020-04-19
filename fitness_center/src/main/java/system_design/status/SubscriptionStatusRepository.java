package system_design.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionStatusRepository extends JpaRepository<SubscriptionStatusDao, Integer> {
}
