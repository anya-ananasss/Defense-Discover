package backend.academy.modelservice.repository;

import backend.academy.modelservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findFirstByTopicAndQuestionIdNotIn(String topic, List<Long> ids);
}
