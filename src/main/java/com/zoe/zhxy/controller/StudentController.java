package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoe.zhxy.pojo.Student;
import com.zoe.zhxy.service.StudentService;
import com.zoe.zhxy.util.MD5;
import com.zoe.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("删除Student")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("要删除的Student的ID集合") @RequestBody List<Integer> ids
    ) {
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改Student")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("Student对象") @RequestBody Student student
    ) {
        Integer id = student.getId();
        // 新增的Student，需要对密码进行加密
        if (null == id || 0 == id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("分页查询Student")
    @GetMapping("/getStudentByOpr/{current}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("当前页") @PathVariable("current") Integer current,
            @ApiParam("每页条数") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("模糊匹配条件") Student student
    ) {
        Page<Student> page = new Page<>(current, pageSize);
        IPage<Student> students = studentService.getStudentByOpr(page, student);
        return Result.ok(students);
    }
}
