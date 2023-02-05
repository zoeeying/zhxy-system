package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoe.zhxy.pojo.Clazz;
import com.zoe.zhxy.service.ClazzService;
import com.zoe.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Clazz控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    ClazzService clazzService;

    @ApiOperation("删除Clazz")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(@ApiParam("要删除的Clazz的ID集合") @RequestBody List<Integer> ids) {
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改Clazz")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("Clazz对象") @RequestBody Clazz clazz) {
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("分页查询Clazz")
    @GetMapping("/getClazzsByOpr/{current}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("当前页") @PathVariable("current") Integer current,
            @ApiParam("每页条数") @PathVariable("pageSize") Integer pageSize,
            // query查询参数，可以传gradeName、name，用于模糊查询
            @ApiParam("模糊匹配条件") Clazz clazz
    ) {
        Page<Clazz> page = new Page<>(current, pageSize);
        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page, clazz);
        return Result.ok(iPage);
    }
}
