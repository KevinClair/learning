package com.github.kevin.learning.learning_14;

import com.github.kevin.learning.learning_11.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "exampleService",
        url = "http://localhost:8080/user",
        configuration = CustomFeignConfig.class
)
public interface CustomFeignClient {

    @PostMapping("/save")
    User save(@RequestBody User user);

    @GetMapping("/get")
    User get(@RequestParam Integer id);
}
