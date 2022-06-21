package com.xkcoding.rbac.security.repository;

import com.xkcoding.rbac.security.model.UserRole;
import com.xkcoding.rbac.security.model.unionkey.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * User role DAO
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 11:24
 * @updateTime Updated in 2022-06-21 14:00
 */
public interface UserRoleDao extends JpaRepository<UserRole, UserRoleKey>, JpaSpecificationExecutor<UserRole> {

}
