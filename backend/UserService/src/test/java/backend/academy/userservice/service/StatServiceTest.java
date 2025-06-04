package backend.academy.userservice.service;
import backend.academy.userservice.dto.StatCounterWithoutUserDto;
import backend.academy.userservice.dto.StatDto;
import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.model.Category;
import backend.academy.userservice.model.Stat;
import backend.academy.userservice.model.User;
import backend.academy.userservice.repository.CategoryRepository;
import backend.academy.userservice.repository.StatsRepository;
import backend.academy.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatServiceTest {

    @Mock
    private StatsRepository statsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StatService statService;

    private User testUser;
    private Category testCategory;
    private Stat testStat;
    private StatDto testStatDto;
    private List<Stat> testStats;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .username("testUser")
            .build();

        testCategory = Category.builder()
            .id(1L)
            .name("TEST_CATEGORY")
            .build();

        testStat = Stat.builder()
            .id(1L)
            .user(testUser)
            .category(testCategory)
            .counterCounter(5L)
            .allquestions(10L)
            .build();

        testStatDto = StatDto.builder()
            .username("testUser")
            .topic("TEST_CATEGORY")
            .isCorrect(true)
            .build();

        testStats = Arrays.asList(
            testStat,
            Stat.builder()
                .id(2L)
                .user(testUser)
                .category(testCategory)
                .counterCounter(3L)
                .allquestions(6L)
                .build()
        );
    }

    @Test
    void addStatShouldIncrementCounterWhenCorrectAnswerAndUserExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findByName("TEST_CATEGORY")).thenReturn(Optional.of(testCategory));
        when(statsRepository.findByUserAndCategoryName(testUser, "TEST_CATEGORY")).thenReturn(Arrays.asList(testStat));
        when(statsRepository.save(any(Stat.class))).thenReturn(testStat);

        StatDto result = statService.addStat(testStatDto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testUser");
        assertThat(result.getTopic()).isEqualTo("TEST_CATEGORY");
        assertThat(result.isCorrect()).isTrue();
        verify(statsRepository).save(any(Stat.class));
    }

    @Test
    void addStatShouldNotIncrementCounterWhenIncorrectAnswer() {
        testStatDto.setCorrect(false);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findByName("TEST_CATEGORY")).thenReturn(Optional.of(testCategory));
        when(statsRepository.findByUserAndCategoryName(testUser, "TEST_CATEGORY")).thenReturn(Arrays.asList(testStat));
        when(statsRepository.save(any(Stat.class))).thenReturn(testStat);

        StatDto result = statService.addStat(testStatDto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testUser");
        assertThat(result.getTopic()).isEqualTo("TEST_CATEGORY");
        assertThat(result.isCorrect()).isFalse();
        verify(statsRepository).save(any(Stat.class));
    }

    @Test
    void addStatShouldCreateNewStatWhenNoExistingStats() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findByName("TEST_CATEGORY")).thenReturn(Optional.of(testCategory));
        when(statsRepository.findByUserAndCategoryName(testUser, "TEST_CATEGORY")).thenReturn(Arrays.asList());
        when(statsRepository.save(any(Stat.class))).thenReturn(testStat);

        StatDto result = statService.addStat(testStatDto);

        assertThat(result).isNotNull();
        verify(statsRepository).save(any(Stat.class));
    }

    @Test
    void addStatShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> statService.addStat(testStatDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("User not found");

        verify(statsRepository, never()).save(any(Stat.class));
    }

    @Test
    void addStatShouldThrowExceptionWhenCategoryNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findByName("TEST_CATEGORY")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> statService.addStat(testStatDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Category not found");

        verify(statsRepository, never()).save(any(Stat.class));
    }

    @Test
    void getAllStatsShouldReturnListOfStats() {
        when(statsRepository.findAll()).thenReturn(testStats);

        List<StatDtoCounter> result = statService.getAllStats();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testUser");
        assertThat(result.get(0).getTopic()).isEqualTo("TEST_CATEGORY");
        assertThat(result.get(0).getScore()).isEqualTo(5L);
        verify(statsRepository).findAll();
    }

    @Test
    void getUserStatsShouldReturnStatsWhenUserExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(statsRepository.findByUser(testUser)).thenReturn(testStats);

        List<StatCounterWithoutUserDto> result = statService.getUserStats("testUser");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTopic()).isEqualTo("TEST_CATEGORY");
        assertThat(result.get(0).getScore()).isEqualTo(5L);
        assertThat(result.get(0).getAllQuestions()).isEqualTo(10L);
        verify(statsRepository).findByUser(testUser);
    }

    @Test
    void getUserStatsShouldReturnNullWhenUserNotFound() {
        when(userRepository.findByUsername("nonExistent")).thenReturn(Optional.empty());

        List<StatCounterWithoutUserDto> result = statService.getUserStats("nonExistent");

        assertThat(result).isNull();
        verify(statsRepository, never()).findByUser(any());
    }
}