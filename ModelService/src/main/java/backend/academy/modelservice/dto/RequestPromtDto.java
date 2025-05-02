package backend.academy.modelservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestPromtDto {
    private String username;
    private PromtDto promt;
}
