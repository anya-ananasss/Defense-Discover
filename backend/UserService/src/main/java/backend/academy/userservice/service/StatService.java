package backend.academy.userservice.service;

import backend.academy.userservice.dto.StatCounterWithoutUserDto;
import backend.academy.userservice.dto.StatDto;
import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.mapper.StatMapper;
import backend.academy.userservice.model.Category;
import backend.academy.userservice.model.Stat;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.CategoryRepository;
import backend.academy.userservice.repository.StatsRepository;
import backend.academy.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {

    private final StatsRepository statsRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public StatDto addStat(StatDto statDto) {
        User user = userRepository.findByUsername(statDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findByName(statDto.getTopic())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<Stat> stats = statsRepository.findByUserAndCategoryName(user, statDto.getTopic());
        Stat statEntity;

        if(!statDto.isCorrect()) {
            if (stats.isEmpty()) {
                statEntity = Stat.builder()
                        .user(user)
                        .category(category)
                        .counterCounter(0L)
                        .allquestions(1L)
                        .build();
            } else {
                statEntity = stats.get(0);
                statEntity.setCounterCounter(statEntity.getCounterCounter());
                statEntity.setAllquestions(statEntity.getAllquestions() + 1);
            }
            statsRepository.save(statEntity);
            return statDto;
        }

        if (stats.isEmpty()) {
            statEntity = Stat.builder()
                    .user(user)
                    .category(category)
                    .counterCounter(1L)
                    .allquestions(1L)
                    .build();
        } else {
            statEntity = stats.get(0);
            statEntity.setCounterCounter(statEntity.getCounterCounter() + 1);
            statEntity.setAllquestions(statEntity.getAllquestions() + 1);
        }
        statsRepository.save(statEntity);
        return statDto;
    }

    @Transactional(readOnly = true)
    public List<StatDtoCounter> getAllStats() {
        return statsRepository.findAll().stream().map(
                StatMapper::toStatCounterDto
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StatCounterWithoutUserDto> getUserStats(String username) {
        log.info("Получить статистику пользователя:" + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return null;
        }
        User userEntity = userOptional.get();

        List<Stat> stats = statsRepository.findByUser(userEntity);

        return stats.stream().map(
                (stat) -> {
                    return StatCounterWithoutUserDto
                            .builder()
                            .score(stat.getCounterCounter())
                            .topic(stat.getCategory().getName())
                            .allQuestions(stat.getAllquestions())
                            .build();
                }
        ).collect(Collectors.toList());
    }

}
