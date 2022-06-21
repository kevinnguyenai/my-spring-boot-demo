package com.xkcoding.rbac.security.repository;

import com.xkcoding.rbac.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * Authority DAO
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 16:21
 * @updateTime Updated in 2022-06-21 14:00
 */
public interface PermissionDao extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    /**
     * List of query permissions according to the corner table
     *
     * @param ids Character ID list
     * @return Permission list
     */
    @Query(value = "SELECT DISTINCT sec_permission.* FROM sec_permission,sec_role,sec_role_permission WHERE sec_role.id = sec_role_permission.role_id AND sec_permission.id = sec_role_permission.permission_id AND sec_role.id IN (:ids)", nativeQuery = true)
    List<Permission> selectByRoleIdList(@Param("ids") List<Long> ids);
}
