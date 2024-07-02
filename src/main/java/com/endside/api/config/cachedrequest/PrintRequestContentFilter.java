package com.endside.api.config.cachedrequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("ALL")
@Slf4j
@Order
@Component
@Profile({"default","dev"})
@WebFilter(filterName = "printRequestContentFilter", urlPatterns = "/api/*")
public class PrintRequestContentFilter extends OncePerRequestFilter {
    private static final String HEALTH_CHECK_URL = "/hello";
    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = httpServletRequest.getRequestURI();
        if(!requestUrl.startsWith(HEALTH_CHECK_URL)) {
            log.debug("Request URL is: " + requestUrl);
            InputStream inputStream = httpServletRequest.getInputStream();
            byte[] body = StreamUtils.copyToByteArray(inputStream);
            log.debug("Request body is: " + new String(body));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}