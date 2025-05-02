package backend.academy.modelservice.service;

import backend.academy.modelservice.dto.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PromtService {

    List<QuestionDto> getQuestion(RequestPromtDto requestPromtDto);
    CheckAnswerDto postAnswer(PostAnswerDto requestPromtDto);

}
