package com.zoe.zhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zoe.zhxy.pojo.Admin;
import com.zoe.zhxy.pojo.LoginForm;
import com.zoe.zhxy.pojo.Student;
import com.zoe.zhxy.pojo.Teacher;
import com.zoe.zhxy.service.AdminService;
import com.zoe.zhxy.service.StudentService;
import com.zoe.zhxy.service.TeacherService;
import com.zoe.zhxy.util.*;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("请求头中的token") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.fail().message("token失效，请重新登录");
        }

        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        // 从token中获取用户ID和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        if (userId == null || userType == null) {
            return Result.fail().message("token信息有误！");
        }
        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapperAdmin = new QueryWrapper<>();
                queryWrapperAdmin.eq("id", userId.intValue());
                queryWrapperAdmin.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapperAdmin);
                if (admin != null) {
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有误！");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapperStudent = new QueryWrapper<>();
                queryWrapperStudent.eq("id", userId.intValue());
                queryWrapperStudent.eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapperStudent);
                if (student != null) {
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有误！");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapperTeacher = new QueryWrapper<>();
                queryWrapperTeacher.eq("id", userId.intValue());
                queryWrapperTeacher.eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapperTeacher);
                if (teacher != null) {
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有误！");
                }
                break;
        }
        return Result.ok();
    }

    @ApiOperation("上传头像图片")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("头像图片") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        String newFilename = uuid.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

        // 一般会将图片保存到第三方或者独立的图片服务器上
        String portraitPath = "C:/Zoe/Projects/individual/zhxy/target/classes/public/upload/".concat(newFilename);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 响应图片路径
        String path = "upload/".concat(newFilename);
        return Result.ok(path);
    }


    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        // 验证token是否过期
        boolean isExpiration = JwtHelper.isExpiration(token);
        if (isExpiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        // 从token中解析出用户ID、用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        // 验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) && null == sessionVerifiCode) {
            return Result.fail().message("验证码失效，请刷新重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码有误");
        }

        // 从session域中移除现有验证码
        session.removeAttribute("verifiCode");

        // 准备一个map用于存放响应的数据
        Map<String, Object> map = new LinkedHashMap<>();

        // 区分用户类型，查不同的数据库表，进行登录校验
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    // admin不是null，说明在数据库中找到用户了
                    // 然后把用户ID、用户类型转换成一个密文，以token形式向客户端返回
                    if (null != admin) {
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();

        // 获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());

        // 将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);

        // 将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
