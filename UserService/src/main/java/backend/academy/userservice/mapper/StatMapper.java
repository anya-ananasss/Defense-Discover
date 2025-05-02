package backend.academy.userservice.mapper;

import backend.academy.userservice.dto.StatDto;
import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.model.Stat;


public class StatMapper {

    public  static StatDtoCounter toStatCounterDto(Stat stat) {
        return StatDtoCounter
                .builder()
                .topic(stat.getCategory().getName())
                .username(stat.getUser().getUsername())
                .score(stat.getCounterCounter())
                .build();
    }
}
