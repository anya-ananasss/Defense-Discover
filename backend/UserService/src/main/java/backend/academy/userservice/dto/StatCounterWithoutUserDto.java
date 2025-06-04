package backend.academy.userservice.dto;

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