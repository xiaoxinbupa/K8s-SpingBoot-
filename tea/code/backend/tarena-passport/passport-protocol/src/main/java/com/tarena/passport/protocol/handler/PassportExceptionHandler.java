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

package com.tarena.passport.protocol.handler;

import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.result.JsonResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PassportExceptionHandler {

    @ExceptionHandler
    public JsonResult<Void> handleServiceException(PassportBusinessException e) {
        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public JsonResult<Void> handleConstraintViolationException(MethodArgumentNotValidException e) {

        return JsonResult.fail("2009","请正确输入信息");
    }
}
