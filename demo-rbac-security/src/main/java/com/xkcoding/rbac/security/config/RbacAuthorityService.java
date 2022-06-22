package com.xkcoding.rbac.security.config;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xkcoding.rbac.security.common.Consts;
import com.xkcoding.rbac.security.common.Status;
import com.xkcoding.rbac.security.exception.SecurityException;
import com.xkcoding.rbac.security.model.Permission;
import com.xkcoding.rbac.security.model.Role;
import com.xkcoding.rbac.security.repository.PermissionDao;
import com.xkcoding.rbac.security.repository.RoleDao;
import com.xkcoding.rbac.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Dynamic routing authentication
 * </p>
 *
 * @author yangkai.shen, kevinnguyenai
 * @date Created in 2018-12-10 17:17
 * @updateTime Updated in 2022-06-21 15:00
 */
@Component
public class RbacAuthorityService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RequestMappingHandlerMapping mapping;

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        checkRequest(request);

        Object userInfo = authentication.getPrincipal();
        boolean hasPermission = false;

        if (userInfo instanceof UserDetails) {
            UserPrincipal principal = (UserPrincipal) userInfo;
            Long userId = principal.getId();

            List<Role> roles = roleDao.selectByUserId(userId);
            List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);

            // Obtain resources, separate the front and rear end, so the filter page faces the right, only the button permissions are retained
            List<Permission> btnPerms = permissions.stream()
                    // Filter page face permanent
                    .filter(permission -> Objects.equals(permission.getType(), Consts.BUTTON))
                    // filter URL Is empty
                    .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                    // filter METHOD Is empty
                    .filter(permission -> StrUtil.isNotBlank(permission.getMethod())).collect(Collectors.toList());

            for (Permission btnPerm : btnPerms) {
                AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(btnPerm.getUrl(), btnPerm.getMethod());
                if (antPathMatcher.matches(request)) {
                    hasPermission = true;
                    break;
                }
            }

            return hasPermission;
        } else {
            return false;
        }
    }

    /**
     * Check whether the request exists
     *
     * @param request ask
     */
    private void checkRequest(HttpServletRequest request) {
        // Get the current request Methods
        String currentMethod = request.getMethod();
        Multimap<String, String> urlMapping = allUrlMapping();

        for (String uri : urlMapping.keySet()) {
            // pass AntPathRequestMatcher match url
            // able tWay of creation 2 Way of creation AntPathRequestMatcher
            // 1：new AntPathRequestMatcher(uri,method)This method can directly determine whether the method matches, because we do not match the method here
            // Customized throwing, so we use the second way to create
            // 2：new AntPathRequestMatcher(uri) This method does not check the request method, only check the request path
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(uri);
            if (antPathMatcher.matches(request)) {
                if (!urlMapping.get(uri).contains(currentMethod)) {
                    throw new SecurityException(Status.HTTP_BAD_METHOD);
                } else {
                    return;
                }
            }
        }

        throw new SecurityException(Status.REQUEST_NOT_FOUND);
    }

    /**
     * Get everythingURL Mapping，Return format{"/test":["GET","POST"],"/sys":["GET","DELETE"]}
     *
     * @return {@link ArrayListMultimap} Format URL Mapping
     */
    private Multimap<String, String> allUrlMapping() {
        Multimap<String, String> urlMapping = ArrayListMultimap.create();

        // Obtainurl Correspondence information with class and methods
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((k, v) -> {
            // Get the current key Get all below URL
            Set<String> url = k.getPatternsCondition().getPatterns();
            RequestMethodsRequestCondition method = k.getMethodsCondition();

            // For each of URL Add all the request methods
            url.forEach(s -> urlMapping.putAll(s,
                    method.getMethods().stream().map(Enum::toString).collect(Collectors.toList())));
        });

        return urlMapping;
    }
}
