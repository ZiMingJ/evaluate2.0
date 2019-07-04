package com.eddy.evaluate.controller;

import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.service.SysUserService;
import com.eddy.evaluate.util.CustomMap;
import com.eddy.evaluate.util.JsonResult;
import com.eddy.evaluate.util.JwtHelper;
import com.eddy.evaluate.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 用户注册登录
 */
@RequestMapping("/index")
@RestController
public class LoginAndRegisterController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private JwtHelper jwtHelper;


    /**
     * 登录
     *
     * @param no
     * @param pwd
     * @return
     */
    @PostMapping("/login")
    public JsonResult login(String no, String pwd, Integer type) {
        if (!ObjectUtil.checkParamsNotNull(no, pwd)) {
            return JsonResult.errorForEmpty();
        }
        SysUser user = userService.getUserByNumber(no);
        if (user == null) {
            return JsonResult.error("用户不存在");
        }
        if (!user.getPassword().equals(pwd)) {
            return JsonResult.error("密码错误");
        }
        if (!Objects.equals(type, user.getUserType())) {
            return JsonResult.error("所登录的角色禁止登录");
        }
        String token = jwtHelper.generateTokenJwt(user.getNoNumber());
        user.setPassword(null);
        return JsonResult.success(CustomMap.create("token", token).put("data", user));
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public JsonResult register(@ModelAttribute SysUser user) {
        if (user == null || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getNoNumber())
                || StringUtils.isBlank(user.getPassword())) {
            return JsonResult.errorForEmpty();
        }
        SysUser oldUser = userService.getUserByNumber(user.getNoNumber());
        if (oldUser != null) {
            return JsonResult.error("该用户已被注册");
        }
        userService.register(user);
        return JsonResult.actionSuccess();
    }

}
