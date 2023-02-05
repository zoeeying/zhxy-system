package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoe.zhxy.pojo.Admin;
import com.zoe.zhxy.service.AdminService;
import com.zoe.zhxy.util.MD5;
import com.zoe.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    AdminService adminService;

    @ApiOperation("分页查询Admin")
    @GetMapping("/getAllAdmin/{current}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("当前页") @PathVariable("current") Integer current,
            @ApiParam("每页条数") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("模糊匹配条件") String adminName
    ) {
        Page<Admin> page = new Page<>(current, pageSize);
        IPage<Admin> iPage = adminService.getAdminsByOpr(page, adminName);
        return Result.ok(iPage);
    }

    @ApiOperation("新增或修改Admin")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@ApiParam("Admin对象") @RequestBody Admin admin) {
        // 新增的Admin，需要对密码进行加密
        Integer id = admin.getId();
        if (null == id || 0 == id) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @ApiOperation("删除Admin")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@ApiParam("要删除的Admin的ID集合") @RequestBody List<Integer> ids) {
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
