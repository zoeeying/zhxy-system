package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoe.zhxy.pojo.Student;
import com.zoe.zhxy.service.StudentService;
import com.zoe.zhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

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
