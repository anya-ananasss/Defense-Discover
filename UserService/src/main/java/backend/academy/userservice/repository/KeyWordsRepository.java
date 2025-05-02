package backend.academy.userservice.repository;

import backend.academy.userservice.model.KeyWords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyWordsRepository extends JpaRepository<KeyWords, Long> {
}
