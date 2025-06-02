package backend.academy.userservice.service;

import backend.academy.userservice.model.Category;
import backend.academy.userservice.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("history")
                .build();
    }

    @Test
    void shouldInitializeDefaultCategories() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        categoryService.init();

        verify(categoryRepository, times(4)).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getAllValues())
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("history", "science", "culture", "nature");
    }

    @Test
    void shouldNotCreateCategoryIfAlreadyExists() {
        when(categoryRepository.findByName("history")).thenReturn(Optional.of(testCategory));
        when(categoryRepository.findByName(argThat(name -> !name.equals("history")))).thenReturn(Optional.empty());

        categoryService.init();

        verify(categoryRepository, times(3)).save(any(Category.class));
        verify(categoryRepository, never()).save(argThat(category -> 
            category.getName().equals("history")
        ));
    }
} 