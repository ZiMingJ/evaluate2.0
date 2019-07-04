package com.eddy.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eddy.evaluate.entity.SysCourseStudent;
import com.eddy.evaluate.entity.SysCourseTeacher;
import com.eddy.evaluate.dao.SysCourseTeacherMapper;
import com.eddy.evaluate.entity.SysGrade;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.exception.TCException;
import com.eddy.evaluate.service.SysCourseStudentService;
import com.eddy.evaluate.service.SysCourseTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eddy.evaluate.service.SysGradeService;
import com.eddy.evaluate.util.ContextUtil;
import com.eddy.evaluate.util.CustomMap;
import com.eddy.evaluate.util.EntityParamsAutoWrite;
import com.eddy.evaluate.util.JsonResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * <p>
 * 教师课程绑定表 服务实现类
 * </p>
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysCourseTeacherServiceImpl extends ServiceImpl<SysCourseTeacherMapper, SysCourseTeacher> implements SysCourseTeacherService {

    @Autowired
    private SysCourseStudentService courseStudentService;


    @Autowired
    private SysGradeService gradeService;

    /**
     * 绑定课程 给教师
     *
     * @param user
     * @param courseIds
     * @return
     */
    @Override
    public boolean addBind(SysUser user, List<String> courseIds) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("t_id", user.getNoNumber());
        boolean remove = this.remove(wrapper);
        if (!remove) {
            return false;
        }
        for (String courseId : courseIds) {
            SysCourseTeacher courseTeacher = new SysCourseTeacher();
            courseTeacher.setCourseNo(courseId)
                    .setTId(user.getNoNumber());
            EntityParamsAutoWrite.addForPc(courseTeacher);
            boolean save = this.save(courseTeacher);
            if (!save) {
                throw new TCException("绑定失败");
            }
        }
        //排查是有用需要删除的关联信息
        List<SysCourseStudent> studentList = courseStudentService.list(null);
        for (SysCourseStudent student : studentList) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("course_no", student.getCourseNo());
            queryWrapper.eq("t_id", student.getTId());
            if (this.count(queryWrapper) <= 0) {
                //删除掉需要
                boolean b = courseStudentService.removeById(student);
                if (!b) {
                    throw new TCException("课程添加失败");
                }
            }
        }
        return true;
    }


    /**
     * 获取老师的课程数据
     *
     * @return
     */
    @Override
    public JsonResult getTeacherStudy() {
        //随机数生成
        Random random = new Random();
        //获取课程集合
        List<Map<String, Object>> list = baseMapper.selectTeacherStudy(ContextUtil.getUserId());
        for (Map<String, Object> item : list) {
            //获取评价统计
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("course_no", MapUtils.getString(item, "course_no"));
            wrapper.eq("t_id", ContextUtil.getUserId());
            List<SysGrade> gradeList = gradeService.list(wrapper);

            item.put("levelTd", CustomMap.create("le1", computerValue(gradeList, "levelTd", 1))
                    .put("le2", computerValue(gradeList, "levelTd", 2))
                    .put("le3", computerValue(gradeList, "levelTd", 3))
                    .put("le4", computerValue(gradeList, "levelTd", 4))
                    .put("le5", computerValue(gradeList, "levelTd", 5))
            );

            item.put("levelCg", CustomMap.create("le1", computerValue(gradeList, "levelCg", 1))
                    .put("le2", computerValue(gradeList, "levelCg", 2))
                    .put("le3", computerValue(gradeList, "levelCg", 3))
                    .put("le4", computerValue(gradeList, "levelCg", 4))
                    .put("le5", computerValue(gradeList, "levelCg", 5))
            );

            item.put("levelFs", CustomMap.create("le1", computerValue(gradeList, "levelFs", 1))
                    .put("le2", computerValue(gradeList, "levelFs", 2))
                    .put("le3", computerValue(gradeList, "levelFs", 3))
                    .put("le4", computerValue(gradeList, "levelFs", 4))
                    .put("le5", computerValue(gradeList, "levelFs", 5))
            );

            item.put("levelSb", CustomMap.create("le1", computerValue(gradeList, "levelSb", 1))
                    .put("le2", computerValue(gradeList, "levelSb", 2))
                    .put("le3", computerValue(gradeList, "levelSb", 3))
                    .put("le4", computerValue(gradeList, "levelSb", 4))
                    .put("le5", computerValue(gradeList, "levelSb", 5))
            );
            int max = gradeList.size() > 10 ? 10 : gradeList.size();
            Set<Integer> indexList = Sets.newHashSet();
            for (int i = 0; i < max; i++) {
                int index = i;
                if (gradeList.size() < 10) {
                    index = random.nextInt(max);
                    while (indexList.contains(index)) {
                        index = random.nextInt(max);
                    }
                }
                indexList.add(index);
            }
            //构造随机评论的
            List<String> commitList = Lists.newArrayList();
            item.put("commitList", commitList);
            for (Integer i : indexList) {
                commitList.add(gradeList.get(i).getCommit());
            }
        }
        return JsonResult.success(list);
    }

    private String computerValue(List<SysGrade> gradeList, String col, Integer val) {
        if (gradeList.isEmpty()) {
            return "0";
        }
        long count = gradeList.stream().filter(g -> {
            Object o = null;
            try {
                Field field = g.getClass().getDeclaredField(col);
                field.setAccessible(true);
                o = field.get(g);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return o.equals(val);
        }).count();
        return proportionInt(Integer.valueOf(count + ""), gradeList.size());
    }

    /**
     * 计算百分比 整数
     *
     * @param divisor
     * @param dividend
     * @return
     */
    public static String proportionInt(Integer divisor, Integer dividend) {
        if (dividend == null || divisor == null) {
            return null;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format(((float) divisor / (float) dividend) * 100);
        return result;
    }


}
