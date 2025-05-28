package com.github.kevin.learning.lerning_13;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private String code;

    private String msg;

    private T data;
}
