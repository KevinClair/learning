package com.github.kevin.learning.learning_19;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {

    @RequestMapping("/test")
    @Permission("hasPermissions('can_do1') or hasRoles('admin1')")
    public String testPermission() {
        return "success";
    }
}
