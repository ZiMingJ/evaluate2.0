package com.eddy.evaluate.service;

import com.eddy.evaluate.entity.SysCourse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 */
public interface SysCourseService extends IService<SysCourse> {

    SysCourse getCourseByNo(String no);
}
