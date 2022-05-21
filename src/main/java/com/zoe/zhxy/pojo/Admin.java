package com.zoe.zhxy.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_admin")
public class Admin {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private char gender;

    private String password;

    private String email;

    private String telephone;

    private String address;

    // 头像的图片路径
    private String portraitPath;

}
