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
package com.tarena.passport.protocol.enums;

import com.tedu.inn.protocol.enums.ErrorSupport;

public enum ErrorEnum implements ErrorSupport {
    OK("0", "success"),
    BIND_ERROR("1", "非法入参"),
    SYSTEM_ERROR("-1", "system error"),

    //全局
    TOKEN_EXPIRES("1000", "登录过期,请重新登录"),
    TOKEN_PASSWORD_ERROR("1001", "用户密码错误"),

    SYS_USER_DISABLE("2000", "用户已被禁用"),
    SYS_USER_NON_EXISTENT("2001", "用户不存在"),
    SYS_USER_PERMISSION_DENIED("2002", "权限不足"),
    SYS_USER_ALREADY_EXISTS("2003", "用户名存在"),
    SYS_PHONE_ALREADY_EXISTS("2004", "手机号已注册"),
    SYS_MAILBOX_ALREADY_EXISTS("2005", "邮箱已注册"), VALID_CODE_OUT_LIMIT("3001", "图片验证码超出限制"),
    EMPTY_VALID_CODE("3002", "请先生成验证码"),
    INVALID_VCODE("3003", "验证码不正确"),
    UNAUTHORIZAED("4001","用户未登录"),
    JWT_NOT_FOUND("-999","用户未登录");
    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    @Override public String getCode() {
        return this.code;
    }

    @Override public String getMessage() {
        return this.message;
    }
}
