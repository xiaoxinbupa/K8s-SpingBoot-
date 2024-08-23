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

package com.tarena.passport.protocol.pojo.param;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserLoginParam {
    @ApiModelProperty(name = "用户名", value = "username",required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "密码", value = "password",required = true)
    private String password;
    @ApiModelProperty(name = "vcode", value = "图片验证码数字",required = true)
    @NotBlank(message = "验证码不能空")
    private String vcode;

}
