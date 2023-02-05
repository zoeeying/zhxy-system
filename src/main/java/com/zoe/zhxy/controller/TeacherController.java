package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoe.zhxy.pojo.Teacher;
import com.zoe.zhxy.service.TeacherService;
import com.zoe.zhxy.util.MD5;
import com.zoe.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @ApiOperation("分页查询Teacher")
    @GetMapping("/getTeachers/{current}/{pageSize}")
    public Result getTeachers(
            @ApiParam("当前页") @PathVariable("current") Integer current,
            @ApiParam("每页条数") @PathVariable("pageSize") Integer pageSize,
            Teacher teacher
    ) {
        Page<Teacher> page = new Page<>(current, pageSize);
        IPage<Teacher> iPage = teacherService.getTeachersByOpr(page, teacher);
        return Result.ok(iPage);
    }

    @ApiOperation("新增或修改Teacher")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@ApiParam("Teacher对象") @RequestBody Teacher teacher) {
        // 新增的Teacher，需要对密码进行加密
        Integer id = teacher.getId();
        if (null == id || 0 == id) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("删除Teacher")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@ApiParam("要删除的Teacher的ID集合") @RequestBody List<Integer> ids) {
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
