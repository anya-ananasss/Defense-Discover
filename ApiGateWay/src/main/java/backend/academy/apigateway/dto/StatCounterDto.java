package backend.academy.apigateway.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatCounterDto {
    private String username;
    private String topic;
    private Long score;
}
