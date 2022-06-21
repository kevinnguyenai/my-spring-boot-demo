package com.xkcoding.rbac.security.payload;

import lombok.Data;

/**
 * <p>
 * Pagling request parameters
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-12 18:05
 * @updateTime Updated in 2022-06-20 14:00
 */
@Data
public class PageCondition {
    /**
     * Current page number
     */
    private Integer currentPage;

    /**
     * Number per page
     */
    private Integer pageSize;

}
