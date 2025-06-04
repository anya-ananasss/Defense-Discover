package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.StatCounterDto;
import backend.academy.apigateway.dto.StatCounterWithoutUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "stats-client", url = "${stat.client.url}")
public interface StatClient {

    @GetMapping("/getStats")
    List<StatCounterDto> getAllStats();

    @GetMapping("/getUserStat")
    List<StatCounterWithoutUserDto> getUserStat(@RequestParam(name = "username") String username);

}
