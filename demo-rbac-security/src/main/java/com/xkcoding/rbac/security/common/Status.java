package com.xkcoding.rbac.security.common;

import lombok.Getter;

/**
 * <p>
 * Universal status code
 * </p>
 *
 * @author yangkai.shen , kevinnguyenai
 * @date Created in 2018-12-07 14:31
 * @updateTime Updated in 2022-06-20 14:00
 */
@Getter
public enum Status implements IStatus {
    /**
     * Successful operation!
     */
    SUCCESS(200, "Successful operation！"),

    /**
     * Abnormal operation！
     */
    ERROR(500, "Abnormal operation！"),

    /**
     * exit successfully！
     */
    LOGOUT(200, "exit successfully！"),

    /**
     * please log in first!
     */
    UNAUTHORIZED(401, "please log in first！"),

    /**
     * There is no right to visit!
     */
    ACCESS_DENIED(403, "Insufficient permissions！"),

    /**
     * The request does not exist!
     */
    REQUEST_NOT_FOUND(404, "The request does not exist！"),

    /**
     * The request method is not supported!
     */
    HTTP_BAD_METHOD(405, "The request method does not support！"),

    /**
     * Request abnormal!
     */
    BAD_REQUEST(400, "Request abnormalities！"),

    /**
     * The parameters do not match!
     */
    PARAM_NOT_MATCH(400, "The parameter does not match！"),

    /**
     * The parameters cannot be empty!
     */
    PARAM_NOT_NULL(400, "Parameters cannot be empty！"),

    /**
     * The current user has been locked, please contact the administrator to unlock!
     */
    USER_DISABLED(403, "The current user has been locked, please contact the administrator to unlock！"),

    /**
     * wrong user name or password!
     */
    USERNAME_PASSWORD_ERROR(5001, "wrong user name or password！"),

    /**
     * tokenExpired, please log in again!
     */
    TOKEN_EXPIRED(5002, "token Expired, please log in again！"),

    /**
     * tokenIf the analysis fails, try logging in again!
     */
    TOKEN_PARSE_ERROR(5002, "tokenIf the analysis fails, please try to log in again！"),

    /**
     * The current user has logged in elsewhere, please try to change the password
     * or log in again!
     */
    TOKEN_OUT_OF_CTRL(5003,
            "The current user has logged in elsewhere, please try to change the password or log in again！"),

    /**
     * Can't kick yourself manually, try to quit login operation!
     */
    KICKOUT_SELF(5004, "Can't kick yourself manually, try to quit login operation！");

    /**
     * status code
     */
    private Integer code;

    /**
     * returned messages
     */
    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Status fromCode(Integer code) {
        Status[] statuses = Status.values();
        for (Status status : statuses) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return SUCCESS;
    }

    @Override
    public String toString() {
        return String.format(" Status:{code=%s, message=%s} ", getCode(), getMessage());
    }

}
