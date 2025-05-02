package backend.academy.modelservice.client;

import backend.academy.modelservice.dto.PromtDto;
import backend.academy.modelservice.dto.QuestionDto;
import backend.academy.modelservice.dto.RequestPromtDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "promt-client", url = "${promt.client.url}")
public interface PromtClient {

    @PostMapping("/generate_quiz")
    List<QuestionDto> getQuestion(@RequestBody PromtDto requestPromtDto);

}
