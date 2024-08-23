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
package com.tarena.passport.adaptor.filter;

import com.alibaba.fastjson.JSON;
import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.protocol.LoginInfo;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ErrorEnum;
import com.tarena.passport.protocol.result.JsonResult;
import com.tedu.inn.protocol.exception.BusinessException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtRSAGenerator<LoginInfo> jwtRSAGenerator;

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
        if ("/user/login".equals(uri)) {
            chain.doFilter(request, response);
            return;
        }
        String jwt = request.getHeader("Authorization");
        try {

            if (!StringUtils.hasText(jwt) || jwt.length() < 113)
                throw new BusinessException(ErrorEnum.TOKEN_EXPIRES);

            LoginInfo userInfo = jwtRSAGenerator.getLoginFromToken(jwt, LoginInfo.class);

            if (userInfo == null)
                throw new BusinessException(ErrorEnum.TOKEN_EXPIRES);

        } catch (PassportBusinessException e) {
            response.setContentType("text/html;charset=utf-8");
            JsonResult fail = JsonResult.fail(e);
            String result = JSON.toJSONString(fail);
            PrintWriter writer = response.getWriter();
            writer.println(result);
            writer.close();
            return;
        }

        chain.doFilter(request, response);
    }
}
