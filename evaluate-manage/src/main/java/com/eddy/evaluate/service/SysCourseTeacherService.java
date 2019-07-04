package com.eddy.evaluate.service;

import com.eddy.evaluate.entity.SysCourseTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.util.JsonResult;

import java.util.List;

/**
 * <p>
 * 教师课程绑定表 服务类
 * </p>
 *
 */
public interface SysCourseTeacherService extends IService<SysCourseTeacher> {

    boolean addBind(SysUser user, List<String> courseIds);

    JsonResult getTeacherStudy();
}
