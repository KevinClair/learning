package com.github.kevin.learning.learning_11;

import com.baomidou.mybatisplus.annotation.*;
import com.github.kevin.learning.learning_11.annodation.SensitiveEntity;
import com.github.kevin.learning.learning_11.annodation.SensitiveField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user")
@SensitiveEntity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @SensitiveField
    @TableField(updateStrategy = FieldStrategy.ALWAYS) // 总是更新此字段
    private String name;

    private String phone;

    private String email;
}
