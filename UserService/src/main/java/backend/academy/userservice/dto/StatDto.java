package backend.academy.userservice.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatDto {
    private String username;
    private String topic;
}
