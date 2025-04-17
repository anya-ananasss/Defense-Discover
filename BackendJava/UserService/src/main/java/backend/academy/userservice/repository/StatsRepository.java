package backend.academy.userservice.repository;


import org.apache.kafka.common.metrics.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stat, Long> {
}
