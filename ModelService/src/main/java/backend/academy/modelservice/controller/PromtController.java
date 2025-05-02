package backend.academy.modelservice.controller;

import backend.academy.modelservice.client.PromtClient;
import backend.academy.modelservice.dto.CheckAnswerDto;
import backend.academy.modelservice.dto.PostAnswerDto;
import backend.academy.modelservice.dto.QuestionDto;
import backend.academy.modelservice.dto.RequestPromtDto;
import backend.academy.modelservice.service.PromtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PromtController {

    private final PromtService promtService;

    @PostMapping( "/getQuestion")
    public ResponseEntity<List<QuestionDto>> getQuestion(@RequestBody RequestPromtDto requestPromtDto) {
        return ResponseEntity.ok(promtService.getQuestion(requestPromtDto));
    }

    @PostMapping( "/postAnswer")
    public ResponseEntity<CheckAnswerDto> postAnswer(@RequestBody PostAnswerDto postAnswerDto) {
        return ResponseEntity.ok(promtService.postAnswer(postAnswerDto));
    }
}
