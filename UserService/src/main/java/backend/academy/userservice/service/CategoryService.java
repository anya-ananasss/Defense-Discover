package backend.academy.userservice.service;

import backend.academy.userservice.model.Category;
import backend.academy.userservice.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        categoryRepository.save(Category.builder().name("history").build());
        categoryRepository.save(Category.builder().name("science").build());
        categoryRepository.save(Category.builder().name("culture").build());
        categoryRepository.save(Category.builder().name("nature").build());
    }
}
