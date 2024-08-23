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
package com.tarena.tp.tea.admin.param;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuParam {

    @NotEmpty(message = "菜单名称不能为空")
    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("排序")
    private Integer sort;

    @NotNull(message = "类别ID不能为空")
    @ApiModelProperty("类别")
    private Integer categoryId;

    @NotNull(message = "链接类型不能为空")
    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("id")
    private Integer id;

    @NotNull(message = "启用状态不能为空")
    @ApiModelProperty("启用状态")
    private Integer status;

    @ApiModelProperty("文件ID")
    private Integer fileId;

}
