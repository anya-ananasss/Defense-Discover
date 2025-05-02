package backend.academy.modelservice.mapper;

import backend.academy.modelservice.dto.QuestionDto;
import backend.academy.modelservice.model.Question;

import java.util.List;

public class QuestionMapper {

    public static QuestionDto toDto(Question question) {
        return QuestionDto
                .builder()
                .questionId(String.valueOf(question.getQuestionId()))
                .topic(question.getTopic())
                .correctAnswer(question.getCorrectAnswer())
                .question(question.getQuestion())
                .options(List.of(question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4()))
                .build();
    }

}
