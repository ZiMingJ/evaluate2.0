package com.eddy.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.dao.SysUserMapper;
import com.eddy.evaluate.exception.TCException;
import com.eddy.evaluate.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eddy.evaluate.util.EntityParamsAutoWrite;
import com.eddy.evaluate.util.UUIDUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表	 服务实现类
 * </p>
 *
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 获取用户通过工号
     *
     * @param number
     * @return
     */
    @Override
    public SysUser getUserByNumber(String number) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("no_number", number);
        return this.getOne(wrapper);
    }


    /**
     * 查询用户数据 根据类型查询
     *
     * @param type
     * @return
     */
    @Override
    public List<SysUser> getUserByType(Integer type) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_type", type);
        return this.list(wrapper);
    }


    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public SysUser register(SysUser user) {
        user.setId(null);
        EntityParamsAutoWrite.addForPc(user);
        boolean save = this.save(user);
        if (save == false) {
            throw new TCException("注册失败");
        }
        return user;
    }

}
