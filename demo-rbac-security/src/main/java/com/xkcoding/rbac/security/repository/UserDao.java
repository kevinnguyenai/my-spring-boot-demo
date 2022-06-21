package com.xkcoding.rbac.security.repository;

import com.xkcoding.rbac.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * user DAO
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-07 16:18
 * @updateTime Updated in 2022-06-21
 */
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Inquiry users according to the username, mailbox, and mobile phone number
     *
     * @param username username
     * @param email    Mail
     * @param phone    Phone number
     * @return User Info
     */
    Optional<User> findByUsernameOrEmailOrPhone(String username, String email, String phone);

    /**
     * Query the user list according to the user list
     *
     * @param usernameList List
     * @return user list
     */
    List<User> findByUsernameIn(List<String> usernameList);
}
