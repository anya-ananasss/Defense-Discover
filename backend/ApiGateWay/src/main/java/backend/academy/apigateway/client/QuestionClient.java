package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.QuestionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "question-client", url = "${model.client.url}")
public interface QuestionClient {

    @GetMapping("/getAllQuestions")
    List<QuestionDto> getAllQuestions();

    @DeleteMapping("/deleteQuestion/{id}")
    void deleteQuestion(@PathVariable("id") Long id);
}