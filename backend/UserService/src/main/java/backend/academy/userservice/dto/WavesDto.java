package backend.academy.userservice.dto;


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
