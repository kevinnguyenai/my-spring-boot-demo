package com.xkcoding.rbac.security.common;

/**
 * <p>
 * Constant pool
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 15:03
 * @updateTime Updated 2022-06-20 14:00
 */
public interface Consts {
    /**
     * Open up
     */
    Integer ENABLE = 1;
    /**
     * Disable
     */
    Integer DISABLE = 0;

    /**
     * page
     */
    Integer PAGE = 1;

    /**
     * Button
     */
    Integer BUTTON = 2;

    /**
     * JWT exist ReKey preserved in the preservedeyPrefix
     */
    String REDIS_JWT_KEY_PREFIX = "security:jwt:";

    /**
     * Asterisk
     */
    String SYMBOL_STAR = "*";

    /**
     * Email symbol
     */
    String SYMBOL_EMAIL = "@";

    /**
     * Default current page number
     */
    Integer DEFAULT_CURRENT_PAGE = 1;

    /**
     * Default number per page
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * Anonymous user name
     */
    String ANONYMOUS_NAME = "Anonymous User";
}
