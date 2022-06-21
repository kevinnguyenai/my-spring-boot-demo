package com.xkcoding.rbac.security.util;

import com.google.common.collect.Lists;
import com.xkcoding.rbac.security.common.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * RedisTools
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-11 20:24
 * @updateTime Updated in 2022-06-20 14:00
 */
@Component
@Slf4j
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Pagling to get the specified format key, use scan Command replace keys Order
     * can improve query efficiency under the condition of large data volume
     *
     * @param patternKey  keyFormat
     * @param currentPage Current page number
     * @param pageSize    Number per page
     * @return Pagling obtain a specified format key
     */
    public PageResult<String> findKeysForPage(String patternKey, int currentPage, int pageSize) {
        ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection rc = factory.getConnection();
        Cursor<byte[]> cursor = rc.scan(options);

        List<String> result = Lists.newArrayList();

        long tmpIndex = 0;
        int startIndex = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            if (tmpIndex >= startIndex && tmpIndex < end) {
                result.add(key);
            }
            tmpIndex++;
        }

        try {
            cursor.close();
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception e) {
            log.warn("RedisConnect to close abnormality，", e);
        }

        return new PageResult<>(result, tmpIndex);
    }

    /**
     * delete Redis One of the onekey
     *
     * @param key key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * batch deletCertainRedis 中的某些key
     *
     * @param keys Key list
     */
    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }
}
