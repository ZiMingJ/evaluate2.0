/**
 *  判断字符串是否为空
 * @param {*} obj 
 */
function isEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
        return true;
    } else {
        return false;
    }
}

function isNotBlank(str) {
    return str != null && str.length != 0;
}

//数值验证//

/**
 * 邮箱
 * @param {*} s
 */
function isEmail(s) {
    return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
}

/**
 * 手机号码
 * @param {*} s
 */
function isMobile(s) {
    return /^1[0-9]{10}$/.test(s)
}

/**
 * 电话号码
 * @param {*} s
 */
function isPhone(s) {
    return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s)
}

/**
 * URL地址
 * @param {*} s
 */
function isURL(s) {
    return /^http[s]?:\/\/.*/.test(s)
}


$.fn.extend({
    getForm: function () {
        var obj = {};
        var array = $(this).serializeArray();
        $.each(array, function () {
            obj[this.name] = this.value;
        });
        return obj;
    },
    setForm: function (jsonValue) {
        var obj = this;
        $.each(jsonValue, function (name, ival) {
            var $oinput = obj.find("input[name='" + name + "']");
            if ($oinput.attr("type") == "radio" || $oinput.attr("type") == "checkbox") {
                $oinput.each(function () {
                    if (Object.prototype.toString.apply(ival) == '[object Array]') {//是复选框，并且是数组
                        for (var i = 0; i < ival.length; i++) {
                            if ($(this).val() == ival[i])
                                $(this).attr("checked", "checked");
                        }
                    } else {
                        if ($(this).val() == ival)
                            $(this).attr("checked", "checked");
                    }
                });
            } else if ($oinput.attr("type") == "textarea") {//多行文本框
                obj.find("[name=" + name + "]").html(ival);
            } else {
                obj.find("[name=" + name + "]").val(ival);
            }
        });
    }
});

/**
 * 获得URL中的参数
 * @param name
 * @returns {*}
 */
function getQueryParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var reg_rewrite = new RegExp("(^|/)" + name + "/([^/]*)(/|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    var q = window.location.pathname.substr(1).match(reg_rewrite);
    if (r != null) {
        return unescape(r[2]);
    } else if (q != null) {
        return unescape(q[2]);
    } else {
        return null;
    }
}