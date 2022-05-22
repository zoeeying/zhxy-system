package com.zoe.zhxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoe.zhxy.pojo.LoginForm;
import com.zoe.zhxy.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);
}
