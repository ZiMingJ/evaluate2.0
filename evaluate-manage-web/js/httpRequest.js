/**
 * Http请求
 */


//服务器地址
var httpServer = "http://127.0.0.1:8111";

//请求后台统一入口
var baseUrl = httpServer + "/api/";

//后台登录注册用url地址
var loginUrl = httpServer + "/index/"

//token存储Head头和本地存储的Key名
var tokenKey = "Authorization";

var http = axios.create({
    timeout: 1000 * 30,
    withCredentials: true,//跨域
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})

/** 
* 清除登录信息
*/
function clearLoginInfo() {
    localStorage.removeItem(tokenKey);
}

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
    // 请求头带上token
    config.headers['Authorization'] = localStorage.getItem(tokenKey);
    return config
}, error => {
    return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
    if (response.data && response.data.errorCode == "401") { // 401, token失效
        clearLoginInfo()
        window.location.replace("../html/MainInterface.html");
    }
    return response
}, error => {
    return Promise.reject(error)
})


/**
 * 封装Get请求
 * @param url
 * @param params
 * @param success
 * @param error
 */
http.getRequest = (url, params = {}, success, error) => {
    http({
        url: baseUrl + url,
        method: "GET",
        params: params
    }).then(({ data }) => {
        if (data.errorCode == "0") {
            if (success != null) {
                success(data.result);
            }
        } else {
            if (error != null) {
                error(data.errorMessage);
            } else {
                vm.$message({
                    showClose: true,
                    message: data.errorMessage,
                    type: 'error'
                });
            }
        }
    })
}

/**
 * 封装普通Post请求 Body JSON 格式的请求
 * @param url
 * @param body
 * @param success
 * @param error
 */
http.postRequestForBody = (url, body = {}, success, error) => {
    http({
        url: baseUrl + url,
        method: "POST",
        data: body
    }).then(({ data }) => {
        if (data.errorCode == "0") {
            if (success != null) {
                success(data.result);
            }
        } else {
            if (error != null) {
                error(data.errorMessage);
            } else {
                vm.$message({
                    showClose: true,
                    message: data.errorMessage,
                    type: 'error'
                });
            }
        }
    })
}

/**
 * 封装普通Form表单的Post请求
 * @param url
 * @param params
 * @param success
 * @param error
 */
http.postRequest = (url, params = {}, success, error) => {
    http({
        url: baseUrl + url,
        method: "POST",
        params: params
    }).then(({ data }) => {
        if (data.errorCode == "0") {
            if (success != null) {
                success(data.result);
            }
        } else {
            if (error != null) {
                error(data.errorMessage);
            } else {
                vm.$message({
                    showClose: true,
                    message: data.errorMessage,
                    type: 'error'
                });
            }
        }
    })
}

/**
 * 登录操作
 * @param {用户工号或者学号} uid 
 * @param {密码} pwd 
 * @param {类型} type 
 * @param {回调} success
 */
function login(uid, pwd, type, success) {
    if (isEmpty(uid) || isEmpty(pwd)) {
        vm.$message.error("请输入完整");
        return;
    }
    http({
        url: loginUrl + "login",
        method: "POST",
        params: {
            no: uid,
            pwd: pwd,
            type: type
        }
    }).then(({ data }) => {
        if (data.errorCode == "0") {
            var result = data.result;
            localStorage.setItem(tokenKey, result.token);
            success(result.data);
        } else {
            vm.$message({
                showClose: true,
                message: data.errorMessage,
                type: 'error'
            });
        }
    })
}

/**
 * 注册接口
 * @param {姓名} name 
 * @param {工号} uid 
 * @param {密码} pwd 
 * @param {系别} dept 
 * @param {用户类型} userType 
 * @param {手机} phone 
 * @param {性别} sex 
 * @param {成功后的回调} success 
 */
function register(name, uid, pwd, dept, userType, phone, sex, success) {
    if (isEmpty(name)) {
        vm.$message.error("请输入姓名");
        return;
    }
    if (isEmpty(uid)) {
        vm.$message.error("请输入账号");
        return;
    }
    if (isEmpty(pwd)) {
        vm.$message.error("请输入密码");
        return;
    }

    if ((pwd.length >= 6 && pwd.length <= 20) == false) {
        vm.$message.error("密码长度需要到6~20位");
        return;
    }

    if (isMobile(phone) == false) {
        vm.$message.error("手机号码格式不正确");
        return;
    }

    http({
        url: loginUrl + "register",
        method: "POST",
        params: {
            noNumber: uid,
            username: name,
            password: pwd,
            dept: dept,
            userType: userType,
            phone: phone,
            sex: sex
        }
    }).then(({ data }) => {
        if (data.errorCode == "0") {
            success(data.result);
        } else {
            vm.$message({
                showClose: true,
                message: data.errorMessage,
                type: 'error'
            });
        }
    })

}


/**
 * 退出登录
 */
function logOut() {
    clearLoginInfo()
    window.location.replace("../html/MainInterface.html");
}