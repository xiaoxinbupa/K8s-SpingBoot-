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


import com.google.code.kaptcha.Producer;
import com.tarena.passport.common.utils.IPUtils;
import com.tarena.passport.protocol.enums.ErrorEnum;
import com.tarena.passport.provider.constants.Prefix;
import com.tedu.inn.protocol.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "生成验证码图片")
@RestController
@Slf4j
@RequestMapping("/sso")
public class ValiCodeController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private Producer kaptchaProducer;

    @GetMapping("/valid/code")
    @ApiOperation("验证码")
    public void getKaptcha(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response,
        @ApiIgnore HttpSession session) throws BusinessException {
        String ip = IPUtils.getIpAddress(request);
        String sessionId = session.getId();//这个是验证码id值
        log.debug("获取验证码sessionid:{}",sessionId);
        String limitKey = Prefix.SSOPrefixConfiguration.LIMIT_QPS_PREFIX + ip + ":" + sessionId;
        String validCodeKey = Prefix.SSOPrefixConfiguration.VALID_CODE_PREFIX + ip + ":" + sessionId;
        log.debug("存储redis的validCodeKey:{}",validCodeKey);
        //利用set nx实现限流
        //ValueOperations operations = redisTemplate.opsForValue();
        //Boolean enable = operations.setIfAbsent(limitKey, "", 1, TimeUnit.SECONDS);
        /*if (!enable) {
            throw new BusinessException(ErrorEnum.VALID_CODE_OUT_LIMIT);
        }*/
        log.debug("当前登录ip:{}", ip);
        log.debug("当前VALID-CODE-KEY:{}", validCodeKey);
        //生成验证码
        String text = kaptchaProducer.createText();
        session.setAttribute(sessionId,text);
        log.debug("验证码是:{}", text);
        //operations.set(validCodeKey, text, 60, TimeUnit.SECONDS);
        BufferedImage image = kaptchaProducer.createImage(text);
        //response.addHeader("VALID-CODE",validCodeKey);
        response.setHeader("Access-Control-Expose-Headers", "VALID-CODE");
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            throw new BusinessException(ErrorEnum.SYSTEM_ERROR);
        }
    }
}
