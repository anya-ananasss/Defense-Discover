package backend.academy.modelservice.service;

import backend.academy.modelservice.dto.QuestionDto;
import backend.academy.modelservice.model.Question;

import java.util.List;

public interface QuestionService {

    QuestionDto findQuestionAndTopicWhereIdNotIn(List<Long> ids, String topic);

    QuestionDto createQuestion(QuestionDto questionDto);

    void clearQuestions();

    void deleteQuestionById(Long id);

    List<QuestionDto> getAllQuestions();
}
