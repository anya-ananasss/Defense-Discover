package backend.academy.userservice.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StatDto {
    private String username;
    private String topic;
    private boolean isCorrect;
}
