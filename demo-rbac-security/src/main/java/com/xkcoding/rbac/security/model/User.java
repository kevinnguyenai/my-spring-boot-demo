package com.xkcoding.rbac.security.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * user
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 16:00
 * @updateTime Updated in 2022-06-21 14:00
 */
@Data
@Entity
@Table(name = "sec_user")
public class User {

    /**
     * Primary key
     */
    @Id
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * Nick name
     */
    private String nickname;

    /**
     * cell phone
     */
    private String phone;

    /**
     * Mail
     */
    private String email;

    /**
     * Birthday
     */
    private Long birthday;

    /**
     * Gender, Male-1, Female-2
     */
    private Integer sex;

    /**
     * Status, enable -1, disable -0
     */
    private Integer status;

    /**
     * Creation time
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * Update time
     */
    @Column(name = "update_time")
    private Long updateTime;
}
