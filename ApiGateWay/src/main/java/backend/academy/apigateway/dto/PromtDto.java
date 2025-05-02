package backend.academy.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromtDto {
    private String topic;
    private int numQuestions = 1;
    private String difficulty;
    @JsonProperty("key_words")
    private List<String> keyWords;
}
