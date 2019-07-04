package com.eddy.evaluate.controller;

import com.eddy.evaluate.service.SysCourseTeacherService;
import com.eddy.evaluate.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 教师的
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {


    @Autowired
    private SysCourseTeacherService courseTeacherService;

    /**
     * 获取老师的课程数据
     *
     * @return
     */
    @RequestMapping("/getTeacherStudy")
    public JsonResult getTeacherStudy() {
        return courseTeacherService.getTeacherStudy();
    }

}
