package backend.academy.modelservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumPromtDto {
    private String topic;
    private int numQuestions;
    private int difficulty;
    @JsonProperty("key_words")
    private List<String> keyWords;
}
