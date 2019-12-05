package com.tencent.interact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class UrlFilter implements Filter {
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.info(httpRequest.getRequestURI());
        String path = httpRequest.getRequestURI();
        String newPath;
        if (path.contains("/")) {
            if (path.contains("/redirectServer/")) {
                path = path.replace("/redirectServer/","/");
            }
            newPath = "/?url=" + path;
            Map<String, String[]> parameterMap = request.getParameterMap();
            if(parameterMap.size() > 0 ){
                newPath += "?";
            }
            for (String key : parameterMap.keySet()) {
                newPath += key + "=";
                for (String value : parameterMap.get(key)) {
                    newPath += value;
                }
                newPath += "|";
            }
            httpRequest.getRequestDispatcher(newPath.substring(0, newPath.length() - 1)).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}