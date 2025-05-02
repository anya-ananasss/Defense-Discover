package backend.academy.userservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatDtoCounter {
    private String username;
    private String topic;
    private Long score;
}