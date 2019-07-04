package com.eddy.evaluate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 学生评级表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程编号
     */
    @NotBlank(message = "课程编号不可为空")
    private String courseNo;

    /**
     * 教师编号
     */
    @NotBlank(message = "教师编号不可为空")
    private String tId;

    /**
     * 学生编号
     */
    private String sId;

    /**
     * 教师态度 (1.极优，2.优秀，3.良好，4.中等，5.差)
     */
    @NotNull(message = "教师态度不可为空")
    private Integer levelTd;

    /**
     * 教学成果 (1.极优，2.优秀，3.良好，4.中等，5.差)
     */
    @NotNull(message = "教学成果不可为空")
    private Integer levelCg;

    /**
     * 教学方式 (1.极优，2.优秀，3.良好，4.中等，5.差)
     */
    @NotNull(message = "教学方式不可为空")
    private Integer levelFs;

    /**
     * 教学师表 (1.极优，2.优秀，3.良好，4.中等，5.差)
     */
    @NotNull(message = "教学师表不可为空")
    private Integer levelSb;

    /**
     * 评语
     */
    @NotBlank(message = "评语不可为空")
    private String commit;

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
