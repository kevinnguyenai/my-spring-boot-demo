package com.xkcoding.rbac.security.exception.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.xkcoding.rbac.security.common.ApiResponse;
import com.xkcoding.rbac.security.common.BaseException;
import com.xkcoding.rbac.security.common.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * <p>
 * Global unified abnormal treatment
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 17:00
 * @updateTime Updated in 2022-06-21 15:00:00
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse handlerException(Exception e) {
        if (e instanceof NoHandlerFoundException) {
            log.error("【Global abnormal interception】NoHandlerFoundException: Method of requesting {}, Request path {}",
                    ((NoHandlerFoundException) e).getRequestURL(), ((NoHandlerFoundException) e).getHttpMethod());
            return ApiResponse.ofStatus(Status.REQUEST_NOT_FOUND);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.error(
                    "【Global abnormal interception】HttpRequestMethodNotSupportedException: Current request method {}, Support request method {}",
                    ((HttpRequestMethodNotSupportedException) e).getMethod(),
                    JSONUtil.toJsonStr(((HttpRequestMethodNotSupportedException) e).getSupportedHttpMethods()));
            return ApiResponse.ofStatus(Status.HTTP_BAD_METHOD);
        } else if (e instanceof MethodArgumentNotValidException) {
            log.error("【Global abnormal interception】MethodArgumentNotValidException", e);
            return ApiResponse.of(Status.BAD_REQUEST.getCode(),
                    ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                    null);
        } else if (e instanceof ConstraintViolationException) {
            log.error("【Global abnormal interception】ConstraintViolationException", e);
            return ApiResponse.of(Status.BAD_REQUEST.getCode(),
                    CollUtil.getFirst(((ConstraintViolationException) e).getConstraintViolations()).getMessage(), null);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            log.error(
                    "【Global abnormal interception】MethodArgumentTypeMismatchException: parameter name {}, Abnormal information {}",
                    ((MethodArgumentTypeMismatchException) e).getName(),
                    ((MethodArgumentTypeMismatchException) e).getMessage());
            return ApiResponse.ofStatus(Status.PARAM_NOT_MATCH);
        } else if (e instanceof HttpMessageNotReadableException) {
            log.error("【Global abnormal interception】HttpMessageNotReadableException: Error message {}",
                    ((HttpMessageNotReadableException) e).getMessage());
            return ApiResponse.ofStatus(Status.PARAM_NOT_NULL);
        } else if (e instanceof BadCredentialsException) {
            log.error("【Global abnormal interception】BadCredentialsException: Error message {}", e.getMessage());
            return ApiResponse.ofStatus(Status.USERNAME_PASSWORD_ERROR);
        } else if (e instanceof DisabledException) {
            log.error("【Global abnormal interception】BadCredentialsException:Error message {}", e.getMessage());
            return ApiResponse.ofStatus(Status.USER_DISABLED);
        } else if (e instanceof BaseException) {
            log.error("【Global abnormal interception】DataManagerException: status code {}, Abnormal information {}",
                    ((BaseException) e).getCode(), e.getMessage());
            return ApiResponse.ofException((BaseException) e);
        }

        log.error("【Global abnormal interception】: Abnormal information {} ", e.getMessage());
        return ApiResponse.ofStatus(Status.ERROR);
    }
}
