package com.zoe.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoe.zhxy.mapper.AdminMapper;
import com.zoe.zhxy.pojo.Admin;
import com.zoe.zhxy.pojo.LoginForm;
import com.zoe.zhxy.service.AdminService;
import com.zoe.zhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ServiceImpl实现了IService
 * 注解@Transactional表示事务控制？？？
 * "adminServiceImpl"可以作为AdminServiceImpl实现类的ID
 */
@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public Admin login(LoginForm loginForm) {

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        // queryWrapper两个条件满足，会返回查到的那个用户
        // 密码需要加密，因为数据库中保存的密码是加密的
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }
}
