package backend.academy.apigateway.controller;

import backend.academy.apigateway.client.PromtClient;
import backend.academy.apigateway.dto.CheckAnswerDto;
import backend.academy.apigateway.dto.PostAnswerDto;
import backend.academy.apigateway.dto.QuestionDto;
import backend.academy.apigateway.dto.RequestPromtDto;
import backend.academy.apigateway.utils.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PromtController {

    private final PromtClient promtClient;

    @PostMapping(ApiPaths.BASE_API + "/getQuestion")
    public ResponseEntity<List<QuestionDto>> getQuestion(@RequestBody RequestPromtDto requestPromtDto) {
        return ResponseEntity.ok(promtClient.getQuestion(requestPromtDto));
    }

    @PostMapping(ApiPaths.BASE_API + "/postAnswer")
    public ResponseEntity<CheckAnswerDto> postAnswer(@RequestBody PostAnswerDto postAnswerDto) {
        return ResponseEntity.ok(promtClient.postAnswer(postAnswerDto));
    }
}
