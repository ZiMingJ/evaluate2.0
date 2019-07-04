package com.eddy.evaluate.service;

import com.eddy.evaluate.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表	 服务类
 * </p>
 *
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getUserByNumber(String number);

    List<SysUser> getUserByType(Integer type);

    SysUser register(SysUser user);
}
