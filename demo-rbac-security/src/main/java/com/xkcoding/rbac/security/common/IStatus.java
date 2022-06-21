package com.xkcoding.rbac.security.common;

/**
 * <p>
 * REST API Error code interface
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 14:35
 * @updateTime Updated in 2022-06-20 14:00
 */
public interface IStatus {

    /**
     * status code
     *
     * @return status code
     */
    Integer getCode();

    /**
     * returned messages
     *
     * @return returned messages
     */
    String getMessage();

}
