package com.xkcoding.rbac.security.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.xkcoding.rbac.security.common.Consts;
import com.xkcoding.rbac.security.common.PageResult;
import com.xkcoding.rbac.security.model.User;
import com.xkcoding.rbac.security.payload.PageCondition;
import com.xkcoding.rbac.security.repository.UserDao;
import com.xkcoding.rbac.security.util.RedisUtil;
import com.xkcoding.rbac.security.util.SecurityUtil;
import com.xkcoding.rbac.security.vo.OnlineUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * monitor Service
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-12 00:55
 * @updateTime Updated in 2022-06-21 14:00
 */
@Slf4j
@Service
public class MonitorService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDao userDao;

    /**
     * Online user pagination list
     *
     * @param pageCondition Paging parameter
     * @return Online user pagination list
     */
    public PageResult<OnlineUser> onlineUser(PageCondition pageCondition) {
        PageResult<String> keys = redisUtil.findKeysForPage(Consts.REDIS_JWT_KEY_PREFIX + Consts.SYMBOL_STAR,
                pageCondition.getCurrentPage(), pageCondition.getPageSize());
        List<String> rows = keys.getRows();
        Long total = keys.getTotal();

        // Obtain the user list according to the key in Redis
        List<String> usernameList = rows.stream().map(s -> StrUtil.subAfter(s, Consts.REDIS_JWT_KEY_PREFIX, true))
                .collect(Collectors.toList());
        // Query user information according to the username
        List<User> userList = userDao.findByUsernameIn(usernameList);

        // Encourage online user information
        List<OnlineUser> onlineUserList = Lists.newArrayList();
        userList.forEach(user -> onlineUserList.add(OnlineUser.create(user)));

        return new PageResult<>(onlineUserList, total);
    }

    /**
     * Kick out online users
     *
     * @param names List
     */
    public void kickout(List<String> names) {
        // Remove Redis Moderate JWT information
        List<String> redisKeys = names.parallelStream().map(s -> Consts.REDIS_JWT_KEY_PREFIX + s)
                .collect(Collectors.toList());
        redisUtil.delete(redisKeys);

        // Get the current username
        String currentUsername = SecurityUtil.getCurrentUsername();
        names.parallelStream().forEach(name -> {
            // TODO: The user who has been kicked out has been kicked by the current login
            // user，
            // Consider the use of WebSocket in the later period, and the specific pseudo
            // code is achieved as follows。
            // String message = "You have been used by users【" + currentUsername +
            // "】Manually offline!";
            log.debug("user【{}】User【{}ManuallyWire！", name, currentUsername);
        });
    }
}
