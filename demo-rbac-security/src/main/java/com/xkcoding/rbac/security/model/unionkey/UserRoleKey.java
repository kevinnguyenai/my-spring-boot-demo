package com.xkcoding.rbac.security.model.unionkey;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * <p>
 * User-Character Joint Primary Key
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 11:20
 * @updateTime Updated in 2022-06-21 14:00
 */
@Embeddable
@Data
public class UserRoleKey implements Serializable {
    private static final long serialVersionUID = 5633412144183654743L;

    /**
     * user id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * Role id
     */
    @Column(name = "role_id")
    private Long roleId;
}
