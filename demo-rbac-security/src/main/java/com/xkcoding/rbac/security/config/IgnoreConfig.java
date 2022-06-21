package com.xkcoding.rbac.security.config;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Ignore configuration
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-17 17:37
 * @updateTime Updated in 2022-06-20 14:00
 */
@Data
public class IgnoreConfig {
    /**
     * The URL format that needs to be ignored, regardless of the request method
     */
    private List<String> pattern = Lists.newArrayList();

    /**
     * Need to be ignored GET ask
     */
    private List<String> get = Lists.newArrayList();

    /**
     * Need to be ignored POST ask
     */
    private List<String> post = Lists.newArrayList();

    /**
     * Need to be ignored DELETE ask
     */
    private List<String> delete = Lists.newArrayList();

    /**
     * Need to be ignored PUT ask
     */
    private List<String> put = Lists.newArrayList();

    /**
     * Need to be asknored HEAD ask
     */
    private List<String> head = Lists.newArrayList();

    /**
     * Need to be ignored PATCH ask
     */
    private List<String> patch = Lists.newArrayList();

    /**
     * Need to be ignored OPTIONS ask
     */
    private List<String> options = Lists.newArrayList();

    /**
     * Need to be ignored TRACE ask
     */
    private List<String> trace = Lists.newArrayList();
}
