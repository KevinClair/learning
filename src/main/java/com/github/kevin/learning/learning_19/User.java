package com.github.kevin.learning.learning_19;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String name;

    private String userId;

    private List<String> roles;

    private List<String> permissions;
}
