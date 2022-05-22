package com.zoe.zhxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoe.zhxy.mapper.AdminMapper;
import com.zoe.zhxy.pojo.Admin;
import com.zoe.zhxy.service.AdminService;
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

}
