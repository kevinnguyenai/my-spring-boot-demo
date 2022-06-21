package com.xkcoding.rbac.security.controller;

import com.xkcoding.rbac.security.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * Test controller
 * </p>
 *
 * @author yangkai.shen,kevinnguyenai
 * @date Created in 2018-12-10 15:44
 * @updateTime Updated in 2022-06-20 14:00
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public ApiResponse list() {
        log.info("Test list query");
        return ApiResponse.ofMessage("Test list query");
    }

    @PostMapping
    public ApiResponse add() {
        log.info("Test list add");
        return ApiResponse.ofMessage("Test list add");
    }

    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable Long id) {
        log.info("Test list modification");
        return ApiResponse.ofSuccess("Test list modification");
    }
}
