package backend.academy.apigateway.service.impl;

import backend.academy.apigateway.client.QuestionClient;
import backend.academy.apigateway.dto.QuestionDto;
import backend.academy.apigateway.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionClient questionClient;

    @Override
    public List<QuestionDto> getAllQuestions() {
        return questionClient.getAllQuestions();
    }

    @Override
    public void deleteQuestion(Long id) {
        questionClient.deleteQuestion(id);
    }
} 