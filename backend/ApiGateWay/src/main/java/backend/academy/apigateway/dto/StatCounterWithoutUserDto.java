package backend.academy.apigateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatCounterWithoutUserDto {
    private String topic;
    private Long score;
    private Long allQuestions;
}