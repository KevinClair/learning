package com.github.kevin.learning.learning_11;

import com.baomidou.mybatisplus.annotation.*;
import com.github.kevin.learning.learning_11.annodation.SensitiveEntity;
import com.github.kevin.learning.learning_11.annodation.SensitiveField;
import lombok.Data;

@Data
@TableName("user")
@SensitiveEntity
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @SensitiveField
    @TableField(updateStrategy = FieldStrategy.ALWAYS) // 总是更新此字段
    private String name;

    private String phone;

    private String email;
}
