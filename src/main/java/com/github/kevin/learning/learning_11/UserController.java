package com.github.kevin.learning.learning_11;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @PostMapping("/save")
    public User save(@RequestBody User user) {
        userMapper.insert(user);
        return user;
    }

    @PostMapping("/update")
    public User update(@RequestBody User user) {
        userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getName, user.getName()));
        return user;
    }

    @GetMapping("/get")
    public User get(@RequestParam Integer id) {
        return userMapper.selectById(id);
    }
}
