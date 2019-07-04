package com.eddy.evaluate.service;

import com.eddy.evaluate.entity.SysCourseStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eddy.evaluate.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生课程绑定表 服务类
 * </p>
 *
 */
public interface SysCourseStudentService extends IService<SysCourseStudent> {

    boolean addBind(SysUser user, List<Map<String, Object>> studyIds);

    List<Map<String, Object>> selectStudyForStudent();

    List<Map<String,Object>> selectMyStudy(String uid);
}
