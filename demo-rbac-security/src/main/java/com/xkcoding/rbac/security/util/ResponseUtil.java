package com.xkcoding.rbac.security.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xkcoding.rbac.security.common.ApiResponse;
import com.xkcoding.rbac.security.common.BaseException;
import com.xkcoding.rbac.security.common.IStatus;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * Response Universal tool
 * </p>
 *
 * @author yangkai.shen,kevinnguyenai
 * @date Created in 2018-12-07 17:37
 * @updateTime Updated in 2022-06-20 14:00
 */
@Slf4j
public class ResponseUtil {

    /**
     * Pastresponse Write json
     *
     * @param response response
     * @param status   state
     * @param data     Return data
     */
    public static void renderJson(HttpServletResponse response, IStatus status, Object data) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);

            // FIXME: hutool of BUG：JSONUtil.toJsonStr()
            // When converting JSON to string, there is an error in the string converted
            // when I ignore the NULL value
            response.getWriter().write(JSONUtil.toJsonStr(new JSONObject(ApiResponse.ofStatus(status, data), false)));
        } catch (IOException e) {
            log.error("ResponseWriteJSON，", e);
        }
    }

    /**
     * Past response Write json
     *
     * @param response  response
     * @param exception abnormal
     */
    public static void renderJson(HttpServletResponse response, BaseException exception) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);

            // FIXME: hutool of BUG：JSONUtil.toJsonStr()
            // WillJTurn toN转为StrinWhen g, conflictnullTurned when valueStringThere are
            // errors
            response.getWriter().write(JSONUtil.toJsonStr(new JSONObject(ApiResponse.ofException(exception), false)));
        } catch (IOException e) {
            log.error("ResponseWriteJSONabnormal，", e);
        }
    }
}
