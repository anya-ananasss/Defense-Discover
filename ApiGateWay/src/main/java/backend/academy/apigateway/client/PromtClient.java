package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.CheckAnswerDto;
import backend.academy.apigateway.dto.PostAnswerDto;
import backend.academy.apigateway.dto.QuestionDto;
import backend.academy.apigateway.dto.RequestPromtDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "promt-client", url = "${promt.client.url}")
public interface PromtClient {

    @PostMapping("/getQuestion")
    List<QuestionDto> getQuestion(@RequestBody RequestPromtDto requestPromtDto);

    @PostMapping("/postAnswer")
    CheckAnswerDto postAnswer(@RequestBody PostAnswerDto postAnswerDto);
}
