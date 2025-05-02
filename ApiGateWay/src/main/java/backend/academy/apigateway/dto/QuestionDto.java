package backend.academy.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDto {
    private String questionId;
    private String topic;
    @JsonProperty("correct_answer")
    private String correctAnswer;
    private String question;
    private List<String> options;
}
