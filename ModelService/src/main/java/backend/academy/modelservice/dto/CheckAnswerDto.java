package backend.academy.modelservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckAnswerDto {
    private String userName;
    private boolean isCorrect;
    private String correctAnswer;
}
