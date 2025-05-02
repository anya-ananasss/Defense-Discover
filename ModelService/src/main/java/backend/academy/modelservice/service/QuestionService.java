package backend.academy.modelservice.service;

import backend.academy.modelservice.dto.QuestionDto;

import java.util.List;

public interface QuestionService {

    QuestionDto findQuestionAndTopicWhereIdNotIn(List<Long> ids, String topic);

    QuestionDto createQuestion(QuestionDto questionDto);

    void clearQuestions();
}
