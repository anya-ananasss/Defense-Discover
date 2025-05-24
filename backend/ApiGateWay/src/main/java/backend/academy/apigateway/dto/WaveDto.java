package backend.academy.apigateway.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaveDto {
    private String waveCount;
}
