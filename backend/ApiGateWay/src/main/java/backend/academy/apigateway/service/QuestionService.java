package backend.academy.apigateway.service;

import backend.academy.apigateway.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestions();
    void deleteQuestion(Long id);
}