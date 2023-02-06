package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoe.zhxy.pojo.Grade;
import com.zoe.zhxy.service.GradeService;
import com.zoe.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("删除Grade")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的Grade的ID集合") @RequestBody List<Integer> ids) {
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改Grade")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("Grade对象") @RequestBody Grade grade) {
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("分页查询Grade")
    @GetMapping("/getGrades/{current}/{pageSize}")
    public Result getGradesByOpr(
            @ApiParam("当前页") @PathVariable("current") Integer current,
            @ApiParam("每页条数") @PathVariable("pageSize") Integer pageSize,
            // 省略了@RequestParam("gradeName")，该注解默认required是true，省略该注解，gradeName可不传
            @ApiParam("模糊匹配条件") String gradeName
    ) {
        // 分页带条件查询
        Page<Grade> page = new Page<>(current, pageSize);
        IPage<Grade> iPage = gradeService.getGradesByOpr(page, gradeName);
        return Result.ok(iPage);
    }

    @ApiOperation("查询全部Grade")
    @GetMapping("/getGrades")
    public Result getGrades() {
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }
}
