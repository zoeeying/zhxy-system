package com.zoe.zhxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoe.zhxy.pojo.LoginForm;
import com.zoe.zhxy.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);
}
