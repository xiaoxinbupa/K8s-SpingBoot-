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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("用户数据,注册更新使用")
public class UserParam implements Serializable {
    @NotNull(message = "id禁止为空", groups = Check.Update.class)
    @ApiModelProperty(name = "id", value = "用户id,注册非必填", required = false)
    private Long id;
    @NotBlank(message = "请输入用户名！", groups = Check.Create.class)
    @ApiModelProperty(name = "username", value = "用户名", required = true)
    private String username;
    @NotNull(message = "请输入密码！", groups = Check.Create.class)
    @ApiModelProperty(name="passport",value="用户密码",required = true)
    private String password;
    @NotBlank(message="请确认密码",groups=Check.Create.class)
    @ApiModelProperty(name="actPassword",required = true)
    private String actPassword;
    @NotBlank(message = "昵称！", groups = Check.Create.class)
    @ApiModelProperty(name="nickname",required = true)
    private String nickname;
    @NotBlank(message = "请输入手机号！", groups = Check.Create.class)
    @ApiModelProperty(name="phone",required = true)
    private String phone;
    @NotBlank(message = "邮箱！", groups = Check.Create.class)
    @ApiModelProperty(name="email",required = true)
    private String email;
    private Integer enable;
    private String avator;

}
