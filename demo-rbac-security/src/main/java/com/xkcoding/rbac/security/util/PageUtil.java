package com.xkcoding.rbac.security.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.xkcoding.rbac.security.common.Consts;
import com.xkcoding.rbac.security.payload.PageCondition;
import org.springframework.data.domain.PageRequest;

/**
 * <p>
 * Paging tool
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-12 18:09
 * @updateTime Updated in 2022-06-20 14:00
 */
public class PageUtil {
    /**
     * Check the pagination parameter, to NULL, set the default value of the
     * pagination parameter
     *
     * @param condition Query parameter
     * @param clazz     kind
     * @param <T>       {@link PageCondition}
     */
    public static <T extends PageCondition> void checkPageCondition(T condition, Class<T> clazz) {
        if (ObjectUtil.isNull(condition)) {
            condition = ReflectUtil.newInstance(clazz);
        }
        // Check pagination parameter
        if (ObjectUtil.isNull(condition.getCurrentPage())) {
            condition.setCurrentPage(Consts.DEFAULT_CURRENT_PAGE);
        }
        if (ObjectUtil.isNull(condition.getPageSize())) {
            condition.setPageSize(Consts.DEFAULT_PAGE_SIZE);
        }
    }

    /**
     * Construction based on paging parameters{@link PageRequest}
     *
     * @param condition Query parameter
     * @param <T>       {@link PageCondition}
     * @return {@link PageRequest}
     */
    public static <T extends PageCondition> PageRequest ofPageRequest(T condition) {
        return PageRequest.of(condition.getCurrentPage(), condition.getPageSize());
    }
}
