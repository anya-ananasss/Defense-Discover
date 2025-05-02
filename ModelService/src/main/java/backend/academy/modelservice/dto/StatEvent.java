package backend.academy.modelservice.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatEvent {
    private String username;
    private String topic;
}
