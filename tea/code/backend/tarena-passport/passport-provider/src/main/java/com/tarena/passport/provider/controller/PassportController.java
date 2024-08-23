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
package com.tarena.passport.provider.controller;

import com.tarena.passport.common.utils.AgentUtils;
import com.tarena.passport.common.utils.IPUtils;
import com.tarena.passport.doman.service.IUserService;

import com.tarena.passport.protocol.enums.ErrorEnum;
import com.tarena.passport.protocol.pojo.param.Check;
import com.tarena.passport.protocol.pojo.param.UserAddressAndBrowserNameParam;
import com.tarena.passport.protocol.pojo.param.UserLoginParam;
import com.tarena.passport.protocol.pojo.param.UserParam;
import com.tarena.passport.protocol.result.TokenResult;
import com.tarena.passport.provider.constants.Prefix;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(
    value = "用户登录注册",
    tags = "用户登录注册"
)
@Slf4j
public class PassportController {
    @Autowired(required = false)
    private IUserService passportService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<TokenResult> login(@RequestBody UserLoginParam userLoginParam,
        @ApiIgnore HttpServletRequest request,@ApiIgnore HttpSession session) throws BusinessException {

        String address = IPUtils.getIpAddress(request);
        String sessionId = request.getSession().getId();
        log.debug("当前登录sessionId:{}",sessionId);
        String validCodeKey = Prefix.SSOPrefixConfiguration.VALID_CODE_PREFIX + address + ":" + sessionId;
        log.debug("当前登录validCodeKey:{}",validCodeKey);
        ValueOperations operations = stringRedisTemplate.opsForValue();
        String text = (String) session.getAttribute(sessionId);
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(ErrorEnum.EMPTY_VALID_CODE);
        }
        if (!StringUtils.equalsIgnoreCase(text, userLoginParam.getVcode())) {
            throw new BusinessException(ErrorEnum.INVALID_VCODE);
        }
        //stringRedisTemplate.delete(validCodeKey);
        session.removeAttribute(sessionId);
        String browserName = AgentUtils.getLoginAgent(request);
        UserAddressAndBrowserNameParam param = new UserAddressAndBrowserNameParam(address, browserName);
        TokenResult tokenResult = passportService.login(userLoginParam, param, request);
        tokenResult.setHeaderName("Authorization");
        tokenResult.setType("Bearer");
        return new Result<>(tokenResult);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result doRegister(@Validated(Check.Create.class) @RequestBody UserParam userParam,
        HttpServletRequest request) throws BusinessException {
        passportService.addNewUser(userParam, request);
        return Result.success();
    }
}
