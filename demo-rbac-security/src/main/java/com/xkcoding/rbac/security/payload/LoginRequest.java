package com.xkcoding.rbac.security.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * Login request parameter
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 15:52
 * @updateTime Updated in 2022-06-20 14:00
 */
@Data
public class LoginRequest {

    /**
     * Username or mailbox or mobile phone number
     */
    @NotBlank(message = "Username can not be empty")
    private String usernameOrEmailOrPhone;

    /**
     * password
     */
    @NotBlank(message = "password can not be blank")
    private String password;

    /**
     * remember me
     */
    private Boolean rememberMe = false;

}
