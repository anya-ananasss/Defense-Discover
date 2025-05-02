package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.StatCounterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "stats-client", url = "${stat.client.url}")
public interface StatClient {

    @PostMapping("/getStats")
    List<StatCounterDto> getAllStats();

}
