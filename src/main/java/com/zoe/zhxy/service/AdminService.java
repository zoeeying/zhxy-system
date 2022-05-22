package com.zoe.zhxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoe.zhxy.pojo.Admin;
import com.zoe.zhxy.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);
}
