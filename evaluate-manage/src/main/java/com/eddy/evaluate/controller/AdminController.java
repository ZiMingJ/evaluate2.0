package com.eddy.evaluate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eddy.evaluate.entity.SysCourse;
import com.eddy.evaluate.entity.SysCourseStudent;
import com.eddy.evaluate.entity.SysCourseTeacher;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.exception.TCException;
import com.eddy.evaluate.service.*;
import com.eddy.evaluate.util.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 超级管理员的
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysCourseStudentService courseStudentService;

    @Autowired
    private SysCourseTeacherService courseTeacherService;

    @Autowired
    private SysCourseService courseService;

    @Autowired
    private SysGradeService gradeService;

    /**
     * 查询学生或者老师数据
     *
     * @param page
     * @param search
     * @param type   用户类型
     * @return
     */

    @RequestMapping("/getUserList")
    public JsonResult getUserList(Page page, String search, Integer type) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.eq("user_type", type);
        wrapper.and(StringUtils.isNotBlank(search), i -> i.like("username", search)
                .or().like("no_number", search));
        IPage<Object> pageData = userService.page(page, wrapper);
        pageData.setRecords(pageData.getRecords().stream().map(usr -> {
            CustomMap customMap = ObjectUtil.convertObjectToMap(usr);
            //查询是否绑定了课程
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq(type.equals(0) ? "s_id" : "t_id", MapUtils.getString(customMap, "noNumber"));
            int count;
            if (type.equals(0)) {
                count = courseStudentService.count(queryWrapper);
            } else {
                count = courseTeacherService.count(queryWrapper);
            }
            if (count > 0) {
                customMap.put("bindState", true);
                customMap.put("bindCount", count);
            } else {
                customMap.put("bindState", false);
            }
            return customMap;
        }).collect(Collectors.toList()));
        return JsonResult.success(pageData);
    }

    /**
     * 添加课程
     *
     * @param name
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addCourse")
    public JsonResult addCourse(String name) {
        if (StringUtils.isBlank(name)) {
            return JsonResult.errorForEmpty();
        }
        SysCourse course = new SysCourse();
        course.setCourseName(name);
        course.setCourseNo(UUIDUtils.generatePrefixUuid("Course"));
        EntityParamsAutoWrite.addForPc(course);
        boolean save = courseService.save(course);
        return save ? JsonResult.actionSuccess() : JsonResult.actionFailure();
    }

    /**
     * 获得课程数据
     *
     * @param page
     * @param search
     * @return
     */
    @RequestMapping("/getStudyList")
    public JsonResult getStudyList(Page page, String search) {
        QueryWrapper<SysCourse> wrapper = new QueryWrapper();
        wrapper.like(StringUtils.isNotBlank(search), "course_name", search);
        wrapper.orderByDesc("create_time");
        IPage<SysCourse> dataPage = courseService.page(page, wrapper);
        return JsonResult.success(dataPage);
    }

    /**
     * 获取绑定的课程
     *
     * @param uid
     * @return
     */
    @RequestMapping("/getBindStudyList")
    public JsonResult getBindStudyList(String uid) {
        if (StringUtils.isBlank(uid)) {
            return JsonResult.errorForEmpty();
        }
        SysUser sysUser = userService.getUserByNumber(uid);
        if (sysUser == null) {
            return JsonResult.error("用户不存在");
        }
        Integer type = sysUser.getUserType();

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(type.equals(0) ? "s_id" : "t_id", uid);
        List<CustomMap> dataList = Lists.newArrayList();

        if (type.equals(0)) {
            List<SysCourseStudent> scs = courseStudentService.list(wrapper);
            List<Map<String, Object>> myDataList = courseStudentService.selectStudyForStudent();
            for (Map<String, Object> dataItem : myDataList) {
                for (SysCourseStudent sc : scs) {
                    if (sc.getCourseNo().equals(MapUtils.getString(dataItem, "courseNo", ""))
                            && sc.getTId().equals(MapUtils.getString(dataItem, "tId", ""))) {
                        dataItem.put("activate", true);
                        break;
                    } else {
                        dataItem.put("activate", false);
                    }
                }
                dataList.add(CustomMap.converToCustomMap(dataItem));
            }
        } else {
            List<String> bindCId = Lists.newArrayList();
            List<SysCourseTeacher> list = courseTeacherService.list(wrapper);
            for (SysCourseTeacher courseTeacher : list) {
                bindCId.add(courseTeacher.getCourseNo());
            }

            dataList = courseService.list(null).stream().map(c -> {
                CustomMap customMap = ObjectUtil.convertObjectToMap(c);
                customMap.put("uid", uid);
                if (bindCId.contains(c.getCourseNo())) {
                    customMap.put("activate", true);
                } else {
                    customMap.put("activate", false);
                }
                return customMap;
            }).sorted((s1, s2) -> MapUtils.getBooleanValue(s2, "activate") ? 1 : -1).collect(Collectors.toList());
        }

        return JsonResult.success(dataList);
    }

    /**
     * 绑定课程
     *
     * @param params
     * @return
     */
    @PostMapping("/bindStudy")
    public JsonResult bindStudy(@RequestBody Map<String, Object> params) {
        String uid = MapUtils.getString(params, "uid");
        List<String> studyIds = (List<String>) MapUtils.getObject(params, "bindIds");

        if (StringUtils.isBlank(uid) || CollectionUtils.isEmpty(studyIds)) {
            return JsonResult.errorForEmpty();
        }

        SysUser sysUser = userService.getUserByNumber(uid);
        if (sysUser == null) {
            return JsonResult.error("用户不存在");
        }
        if (sysUser.getUserType().equals(0)) {
            //学生的
            //1. 先删除所有的绑定关系 并重新绑定
            Collection<SysCourseTeacher> byIds = courseTeacherService.listByIds(studyIds);
            if (courseStudentService.addBind(sysUser, byIds.stream().map(st -> CustomMap.create()
                    .put("cid", st.getCourseNo())
                    .put("tid", st.getTId())).collect(Collectors.toList()))) {
                return JsonResult.actionSuccess();
            }
        } else {
            //老师的
            //1. 先删除所有的绑定关系 并重新绑定
            if (courseTeacherService.addBind(sysUser, studyIds)) {
                return JsonResult.actionSuccess();
            }
        }
        return JsonResult.actionFailure();
    }


    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("/getUserInfo")
    public JsonResult getUserInfo(String uid) {
        return JsonResult.success(userService.getUserByNumber(uid));
    }


    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public JsonResult updateUser(@RequestBody @Validated SysUser user) {
        if (user == null) {
            return JsonResult.errorForEmpty();
        }
        user.setUserType(null);
        user.setNoNumber(null);
        EntityParamsAutoWrite.updateForPc(user);
        boolean update = userService.updateById(user);
        return update ? JsonResult.actionSuccess() : JsonResult.actionFailure();
    }

    /**
     * 删除用户
     *
     * @param uid
     * @return
     */
    @PostMapping("/deleteUser")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult deleteUser(String uid) {
        SysUser user = userService.getUserByNumber(uid);
        if (user == null) {
            return JsonResult.error("用户不存在");
        }
        if (user.getUserType().equals(0)) {
            //学生
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("s_id", uid);
            //删除学生课程绑定表
            boolean state = courseStudentService.remove(wrapper);
            if (!state) {
                throw new TCException("删除失败");
            }
            //删除评级表信息
            state = gradeService.remove(wrapper);
            if (!state) {
                throw new TCException("删除失败");
            }
        } else {
            //教师
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("t_id", uid);
            //删除教师课程绑定表
            boolean state = courseTeacherService.remove(wrapper);
            if (!state) {
                throw new TCException("删除失败");
            }
            //删除学生课程绑定表
            state = courseStudentService.remove(wrapper);
            if (!state) {
                throw new TCException("删除失败");
            }
            //删除学生评级表
            state = gradeService.remove(wrapper);
            if (!state) {
                throw new TCException("删除失败");
            }
        }
        //删除用户
        boolean state = userService.removeById(user.getId());
        if (!state) {
            throw new TCException("删除失败");
        }
        return JsonResult.actionSuccess();
    }

    /**
     * 更新课程信息
     *
     * @param course
     * @return
     */
    @RequestMapping("/updateStudy")
    public JsonResult updateStudy(@ModelAttribute SysCourse course) {
        if (course == null || StringUtils.isBlank(course.getCourseNo())
                || StringUtils.isBlank(course.getCourseName())) {
            return JsonResult.errorForEmpty();
        }

        SysCourse data = courseService.getCourseByNo(course.getCourseNo());
        if (data == null) {
            return JsonResult.error("课程不存在");
        }
        data.setCourseName(course.getCourseName());
        EntityParamsAutoWrite.updateForPc(data);
        boolean state = courseService.updateById(data);
        return state ? JsonResult.actionSuccess() : JsonResult.actionFailure();
    }

    /**
     * 删除课程信息
     *
     * @param cId
     * @return
     */
    @RequestMapping("/deleteStudy")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult deleteStudy(String cId) {
        if (StringUtils.isBlank(cId)) {
            return JsonResult.errorForEmpty();
        }
        SysCourse course = courseService.getCourseByNo(cId);
        if (course == null) {
            return JsonResult.error("课程不存在");
        }
        //删除学生课程绑定表
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("course_no", cId);
        boolean state = courseStudentService.remove(wrapper);
        if (!state) {
            throw new TCException("删除失败");
        }
        //删除教师课程绑定表
        state = courseTeacherService.remove(wrapper);
        if (!state) {
            throw new TCException("删除失败");
        }
        //删除学生评级表
        state = gradeService.remove(wrapper);
        if (!state) {
            throw new TCException("删除失败");
        }
        state = courseService.removeById(course.getId());
        if (!state) {
            throw new TCException("删除失败");
        }
        return JsonResult.actionSuccess();
    }

}
