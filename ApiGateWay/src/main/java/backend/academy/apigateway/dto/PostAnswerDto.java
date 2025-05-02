package backend.academy.apigateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostAnswerDto {
    private String userName;
    private String questionId;
    private String answer;
}
