package com.xkcoding.rbac.security.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * Role
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 15:45
 * @updateTime Updated in 2022-06-21 15:00:00
 */
@Data
@Entity
@Table(name = "sec_role")
public class Role {
    /**
     * Primary key
     */
    @Id
    private Long id;

    /**
     * character name
     */
    private String name;

    /**
     * describe
     */
    private String description;

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
