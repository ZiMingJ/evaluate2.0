package com.eddy.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eddy.evaluate.entity.SysCourseStudent;
import com.eddy.evaluate.dao.SysCourseStudentMapper;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.exception.TCException;
import com.eddy.evaluate.service.SysCourseStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eddy.evaluate.util.EntityParamsAutoWrite;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生课程绑定表 服务实现类
 * </p>
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysCourseStudentServiceImpl extends ServiceImpl<SysCourseStudentMapper, SysCourseStudent> implements SysCourseStudentService {

    /**
     * 绑定课程 给学生
     *
     * @param user
     * @param studyIds
     * @return
     */
    @Override
    public boolean addBind(SysUser user, List<Map<String, Object>> studyIds) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("s_id", user.getNoNumber());
        boolean remove = this.remove(wrapper);
        if (!remove) {
            return false;
        }
        for (Map<String, Object> map : studyIds) {
            SysCourseStudent courseStudent = new SysCourseStudent();
            courseStudent.setCourseNo(MapUtils.getString(map, "cid"))
                    .setSId(user.getNoNumber())
                    .setTId(MapUtils.getString(map, "tid"));
            EntityParamsAutoWrite.addForPc(courseStudent);
            boolean save = this.save(courseStudent);
            if (!save) {
                throw new TCException("绑定失败");
            }
        }
        return true;
    }


    /**
     * 获取学生可以绑定的课程列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> selectStudyForStudent() {
        return baseMapper.selectStudyForStudent();
    }

    /**
     * 获取我的课程
     *
     * @param uid
     * @return
     */
    @Override
    public List<Map<String, Object>> selectMyStudy(String uid) {
        return baseMapper.selectMyStudy(uid);
    }
}
