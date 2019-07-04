package com.eddy.evaluate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生课程绑定表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysCourseStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程编号
     */
    private String courseNo;

    /**
     * 学生编号
     */
    private String sId;

    /**
     * 老师编号
     */
    private String tId;

    /**
     * 创建时间
     */
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
