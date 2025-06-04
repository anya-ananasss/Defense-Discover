package backend.academy.apigateway.controller;

import backend.academy.apigateway.dto.QuestionDto;
import backend.academy.apigateway.service.QuestionService;
import backend.academy.apigateway.utils.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Управление вопросами", description = "Операции для управления вопросами")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping(ApiPaths.BASE_API + "/admin/questions")
    @Operation(summary = "Получить все вопросы")
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        try {
            return ResponseEntity.ok(questionService.getAllQuestions());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(ApiPaths.BASE_API + "/admin/questions/{id}")
    @Operation(summary = "Удалить вопрос по Id")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 