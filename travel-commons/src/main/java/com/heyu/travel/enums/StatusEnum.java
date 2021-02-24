package com.heyu.travel.enums;

/**
 * @Description  路由状态枚举
 */
public enum StatusEnum {
    /**
     * @Description 状态码及携带信息
     */
    SUCCEED("200","操作成功"),
    FAIL("1000","操作失败"),
    NO_LOGIN("1001", "请登录"),
    JWT_OVERDUE("1007", "请重新登录"),
    REQ_SUCCESS("200", "成功"),
    FIND_USERINF_FAIL("1002", "失败"),
    LOGIN_FAILURE("1003", "登录失败"),
    LOGOUT("1004", "注销成功"),
    NO_AUTH("1005", "权限不足"),
    NO_ROLE("1006", "角色不符合");

    private String code;
    private String msg;

    StatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
