package com.eddy.evaluate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eddy.evaluate.entity.SysGrade;
import com.eddy.evaluate.service.SysCourseStudentService;
import com.eddy.evaluate.service.SysGradeService;
import com.eddy.evaluate.util.ContextUtil;
import com.eddy.evaluate.util.EntityParamsAutoWrite;
import com.eddy.evaluate.util.JsonResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 学生端
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private SysCourseStudentService courseStudentService;

    @Autowired
    private SysGradeService gradeService;

    /**
     * 获取我的课程
     *
     * @return
     */
    @RequestMapping("/getMyStudy")
    public JsonResult getMyStudy() {
        List<Map<String, Object>> list = courseStudentService.selectMyStudy(ContextUtil.getUserId());
        for (Map<String, Object> map : list) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("course_no", MapUtils.getString(map, "courseNo"));
            wrapper.eq("t_id", MapUtils.getString(map, "tId"));
            wrapper.eq("s_id", ContextUtil.getUserId());
            SysGrade sysGrade = gradeService.getOne(wrapper);
            map.put("grade", sysGrade);
        }
        return JsonResult.success(list);
    }


    /**
     * 提交评价
     *
     * @param grade
     * @return
     */
    @PostMapping("/confirm")
    public JsonResult confirm(@RequestBody @Validated SysGrade grade) {
        if (grade == null) {
            return JsonResult.errorForEmpty();
        }
        //查询是否已经评论了
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("course_no", grade.getCourseNo());
        wrapper.eq("t_id", grade.getTId());
        wrapper.eq("s_id", ContextUtil.getUserId());
        if (gradeService.count(wrapper) > 0) {
            return JsonResult.error("您已经评价过改教师,不可重复再次评价");
        }
        //添加评价数据
        grade.setSId(ContextUtil.getUserId());
        EntityParamsAutoWrite.addForPc(grade);
        boolean save = gradeService.save(grade);
        return save ? JsonResult.actionSuccess() : JsonResult.actionFailure();
    }

}
