package com.github.kevin.learning.learning_12;

import com.github.kevin.learning.learning_11.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user1")
@RequiredArgsConstructor
public class UserTestController {


    @PostMapping("/save")
    public User save(@RequestBody User user) {
        return user;
    }

    @PostMapping("/update")
    public User update(@RequestBody User user) {
        return user;
    }

    @GetMapping("/get")
    public User get(@RequestParam Integer id) {
        return new User(1, "mingjie", "12345678901", "");
    }
}
