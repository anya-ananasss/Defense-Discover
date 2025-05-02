package backend.academy.userservice.service;

import backend.academy.userservice.dto.StatDto;
import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.mapper.StatMapper;
import backend.academy.userservice.model.Category;
import backend.academy.userservice.model.Stat;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.CategoryRepository;
import backend.academy.userservice.repository.StatsRepository;
import backend.academy.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatsRepository statsRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public StatDto addStat(StatDto statDto) {

        Optional<User> userOptional = userRepository.findByUsername(statDto.getUsername());

        if(userOptional.isEmpty()) {
            return null;
        }

        User userEntity = userOptional.get();
        List<Stat> stats = statsRepository.findByUserAndCategoryName(userEntity, statDto.getUsername());

        Stat statEntity = stats.getFirst();
        statEntity.setCounterCounter(statEntity.getCounterCounter() + 1);

        statsRepository.save(statEntity);

        return statDto;
    }

    public List<StatDtoCounter> getAllStats() {
        return statsRepository.findAll().stream().map(
                StatMapper::toStatCounterDto
        ).collect(Collectors.toList());
    }

}
