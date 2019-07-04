package com.eddy.evaluate.dao;

import com.eddy.evaluate.entity.SysCourseTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教师课程绑定表 Mapper 接口
 * </p>
 *
 */

public interface SysCourseTeacherMapper extends BaseMapper<SysCourseTeacher> {


    /**
     * 获得老师的课程列表
     *
     * @param tid
     * @return
     */
    List<Map<String, Object>> selectTeacherStudy(String tid);

}
