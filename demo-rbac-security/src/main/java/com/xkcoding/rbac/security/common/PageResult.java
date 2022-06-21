package com.xkcoding.rbac.security.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * GM pagination parameter returns
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-11 20:26
 * @updateTime Updated in 2021-06-20 14:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 3420391142991247367L;

    /**
     * Current page data
     */
    private List<T> rows;

    /**
     * Total number
     */
    private Long total;

    /**
     * 
     * @param <T>
     * @param rows
     * @param total
     * @return current Class Objects by type T of rows and totals
     */
    public static <T> PageResult of(List<T> rows, Long total) {
        return new PageResult<>(rows, total);
    }
}
