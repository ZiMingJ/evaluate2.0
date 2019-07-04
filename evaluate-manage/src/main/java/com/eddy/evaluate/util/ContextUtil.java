package com.eddy.evaluate.util;

import com.eddy.evaluate.constant.Const;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.exception.LoginFailureException;
import org.apache.commons.collections.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public final class ContextUtil {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static final String USER_KEY = "USR_KEY";

    public static void setUser(SysUser user) {
        if (user == null) {
            throw new RuntimeException("Invalid Params");
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(USER_KEY, user);
        THREAD_LOCAL.set(map);
    }

    /**
     * 获取用户
     *
     * @return
     */
    public static SysUser getUser() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (MapUtils.isEmpty(map)) {
            throw new LoginFailureException("请先登录");
        }
        return (SysUser) map.get(USER_KEY);
    }


    public static String getUserId() {
        return getUser().getNoNumber();
    }

    public static void close() {
        THREAD_LOCAL.remove();
    }

}
