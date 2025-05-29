package com.github.kevin.learning.learning_14;

import com.github.kevin.learning.learning_11.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign-test")
public class FeignTestController {

    private final CustomFeignClient feignClient;

    @RequestMapping("/get")
    public User get(@RequestParam Integer id) {
        return feignClient.get(id);
    }

    @PostMapping("/save")
    public User save(@RequestBody User user) {
        return feignClient.save(user);
    }
}
