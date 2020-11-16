package com.smsposterpro.interceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Swagger拦截器
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@Slf4j
@Component
public class SwaggerInterceptor implements HandlerInterceptor {

    @Value("${swagger.enabled:false}")
    private Boolean enabledSwagger;

    @Value("${swagger.redirectUri:/}")
    private String redirectUri;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!enabledSwagger) {
            String uri = request.getContextPath();
            if (StringUtils.isNotBlank(redirectUri))
                uri = request.getContextPath() + redirectUri;
            if (StringUtils.isBlank(uri))
                uri = "/";
            try {
                response.sendRedirect(uri);
            } catch (IOException e) {
                log.error(String.format("Redirect to '%s' for swagger throw an exception : %s", uri, e.getMessage()), e);
            }
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
