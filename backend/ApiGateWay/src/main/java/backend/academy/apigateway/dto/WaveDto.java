package backend.academy.apigateway.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class WaveDto {
    String waveCount;
}
