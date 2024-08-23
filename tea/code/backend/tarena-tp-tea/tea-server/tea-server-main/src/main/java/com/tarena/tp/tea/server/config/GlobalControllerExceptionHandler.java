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

package com.tarena.tp.tea.server.config;

import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.Result;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * 功能描述: controller层的针对@RequestBody报出的异常拦截处理
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return Result.fail(ResultEnum.BIND_ERROR.getCode(), objectError.getDefaultMessage());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler({BusinessException.class})
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return Result.fail(e);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("系统异常", e);
        return Result.fail(ResultEnum.SYSTEM_ERROR);
    }

    /**
     * 处理绑定异常（通过Validation框架验证请求参数时的异常）
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        if (null == e || null == e.getBindingResult().getFieldError()) {
            log.error("数据绑定异常  BindException is null");
            return Result.fail(ResultEnum.SYSTEM_ERROR);
        }
        log.error("数据绑定异常", e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.fail(ResultEnum.BIND_ERROR.getCode(), message);
    }

    /**
     * 处理系统（其它）异常
     */
    @ExceptionHandler({Throwable.class})
    public Result<Void> handleSystemError(Throwable e) {
        log.error("系统异常", e);
        return Result.fail(ResultEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(PassportBusinessException.class)
    public Result<Void> handlePassportBusinessException(Throwable e) {
        log.error("系统异常", e);
        return Result.fail((BusinessException) e);
    }
}
