package com.eddy.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eddy.evaluate.entity.SysCourse;
import com.eddy.evaluate.dao.SysCourseMapper;
import com.eddy.evaluate.service.SysCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 */
@Service
public class SysCourseServiceImpl extends ServiceImpl<SysCourseMapper, SysCourse> implements SysCourseService {


    /**
     * 查询课程数据 根据课程编号
     *
     * @param no
     * @return
     */
    @Override
    public SysCourse getCourseByNo(String no) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("course_no", no);
        return getOne(wrapper);
    }

}
