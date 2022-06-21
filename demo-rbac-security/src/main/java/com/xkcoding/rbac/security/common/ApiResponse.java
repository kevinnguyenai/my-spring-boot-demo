package com.xkcoding.rbac.security.common;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * General API interface package
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 14:55
 * @updateTime Updated in 2022-06-20 14:00
 */
@Data
public class ApiResponse implements Serializable {
    private static final long serialVersionUID = 8993485788201922830L;

    /**
     * status code
     */
    private Integer code;

    /**
     * Return content
     */
    private String message;

    /**
     * Return data
     */
    private Object data;

    /**
     * Non -parameter constructor
     */
    private ApiResponse() {

    }

    /**
     * Fully constructing function
     *
     * @param code    status code
     * @param Message Return to content
     * @param Data    Return to data
     */
    private ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Construct a custom API to return
     * 
     * @param code    status code
     * @param Message Return to content
     * @param Data    Return to data
     * @return ApiResponse
     */
    public static ApiResponse of(Integer code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    /**
     * Construct a successful and without data API to return
     *
     * @return ApiResponse
     */
    public static ApiResponse ofSuccess() {
        return ofSuccess(null);
    }

    /**
     * Construct a successful and data API to return
     *
     * @param data Return data
     * @return ApiResponse
     */
    public static ApiResponse ofSuccess(Object data) {
        return ofStatus(Status.SUCCESS, data);
    }

    /**
     * Construct an API with a successful and customized message
     *
     * @param message Return content
     * @return ApiResponse
     */
    public static ApiResponse ofMessage(String message) {
        return of(Status.SUCCESS.getCode(), message, null);
    }

    /**
     * Construct a state of API to return
     *
     * @param status state{@link Status}
     * @return ApiResponse
     */
    public static ApiResponse ofStatus(Status status) {
        return ofStatus(status, null);
    }

    /**
     * Construct an API with a state and data to return
     *
     * @param statusStatus {@Link isstatus}
     * @param data         Return data
     * @return ApiResponse
     */
    public static ApiResponse ofStatus(IStatus status, Object data) {
        return of(status.getCode(), status.getMessage(), data);
    }

    /**
     * Constructing an abnormal API to return
     *
     * @param t   abnormal
     * @param <T> {@link BaseException} Subclass
     * @return ApiResponse
     */
    public static <T extends BaseException> ApiResponse ofException(T t) {
        return of(t.getCode(), t.getMessage(), t.getData());
    }
}
