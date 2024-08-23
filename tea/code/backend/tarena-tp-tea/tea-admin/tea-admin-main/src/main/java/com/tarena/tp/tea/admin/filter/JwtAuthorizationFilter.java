/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tarena.tp.tea.admin.filter;

import com.alibaba.fastjson.JSON;
import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.protocol.LoginUser;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ErrorEnum;
import com.tedu.inn.protocol.exception.BusinessException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "gateway")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtRSAGenerator<LoginUser> jwtRSAGenerator;

    private List<String> whitelists;

    public List<String> getWhitelists() {
        return whitelists;
    }

    public void setWhitelists(List<String> whitelists) {
        this.whitelists = whitelists;
    }

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @SneakyThrows @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        response.setHeader("Access-Control-Expose-Headers", "*");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        String uri = request.getRequestURI();
        List<String> collect = whitelists.stream().filter(matched ->
                JwtAuthorizationFilter.PATH_MATCHER.match(matched, uri)).collect(Collectors.toList());
        if (collect.size() != 0) {
            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            mutableRequest.putHeader("Authorization", "");
            chain.doFilter(mutableRequest, response);
            return;
        }
        String jwt = request.getHeader("Authorization");
        try {
            if (!StringUtils.hasText(jwt) || jwt.length() < 113) {
                handlerExceptionResolver.resolveException(request, response,
                        null,new BusinessException(ErrorEnum.TOKEN_EXPIRES));
                return;
            }
            LoginUser userInfo = jwtRSAGenerator.getLoginFromToken(jwt, LoginUser.class);
            if (userInfo == null) {
                handlerExceptionResolver.resolveException(request, response,
                        null,new BusinessException(ErrorEnum.TOKEN_EXPIRES));
                return;
            }
            String s = JSON.toJSONString(userInfo);
            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            mutableRequest.putHeader("Authorization", s);
            chain.doFilter(mutableRequest, response);
        } catch (PassportBusinessException e) {
            handlerExceptionResolver.resolveException(request, response,
                    null,e);
            return;

            //response.setContentType("text/html;charset=utf-8");
            //JsonResult fail = JsonResult.fail(e);
            //String result = JSON.toJSONString(fail);
            //PrintWriter writer = response.getWriter();
            //writer.println(result);
            //writer.close();
            //return;
        }
    }
}
