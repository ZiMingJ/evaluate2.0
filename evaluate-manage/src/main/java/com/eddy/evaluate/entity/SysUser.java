package com.eddy.evaluate.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 用户表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号OR工号
     */
    private String noNumber;

    /**
     * 姓名
     */
    private String username;

    /**
     * 性别
     */
    private String sex;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 系别
     */
    private String dept;

    /**
     * 专业
     */
    private String major;

    /**
     * 登录密码
     */
    @NotBlank(message = "登录密码不可为空")
    private String password;

    /**
     * 账号类型(0.学生,1.教师)
     */
    private Integer userType;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy/MM/dd")
    private LocalDateTime createTime;

    /**
     * 创建端
     */
    private String createForm;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建Function名
     */
    private String createMethod;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新端
     */
    private String updateForm;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新Function名
     */
    private String updateMethod;

    /**
     * 是否弃用：1.正常，0.失效
     */
    private Integer loseFlag;


}
