package backend.academy.apigateway.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WavesDto {

    private int countWaves;
    private String username;
}
