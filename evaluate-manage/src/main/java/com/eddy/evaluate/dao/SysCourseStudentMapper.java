package com.eddy.evaluate.dao;

import com.eddy.evaluate.entity.SysCourseStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生课程绑定表 Mapper 接口
 * </p>
 *
 */
public interface SysCourseStudentMapper extends BaseMapper<SysCourseStudent> {


    /**
     * 查询我的所有的课程
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> selectMyCourseList(String userId);

    /**
     * 查询课程
     *
     * @return
     */
    List<Map<String, Object>> selectStudyForStudent();


    /**
     * 获取我的课程
     *
     * @param uid
     * @return
     */
    List<Map<String, Object>> selectMyStudy(String uid);
}
