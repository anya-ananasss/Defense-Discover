package backend.academy.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryRepository, Long> {

    Optional<CategoryRepository> findByName(String name);
}
