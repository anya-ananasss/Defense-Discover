package backend.academy.userservice.repository;

import backend.academy.userservice.model.User;
import backend.academy.userservice.model.Waves;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WavesRepository extends JpaRepository<Waves, Long> {
    Optional<Waves> findByUser(User user);

    List<Waves> findAllByOrderByCountWavesDesc();
}