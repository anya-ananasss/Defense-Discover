package backend.academy.userservice.repository;


import backend.academy.userservice.model.Stat;
import backend.academy.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {

    List<Stat> findByUserAndCategoryName(User user, String categoryName);
    List<Stat> findByUser(User user);

}
