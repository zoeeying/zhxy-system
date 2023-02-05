package com.zoe.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zoe.zhxy.pojo.Grade;

import java.util.List;

public interface GradeService extends IService<Grade> {
    IPage<Grade> getGradesByOpr(Page<Grade> page, String gradeName);

    List<Grade> getGrades();
}
