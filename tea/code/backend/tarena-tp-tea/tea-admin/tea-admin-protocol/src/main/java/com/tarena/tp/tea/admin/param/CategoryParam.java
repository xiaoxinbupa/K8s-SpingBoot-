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
public class CategoryParam {

    @ApiModelProperty("修改时必填")
    private Integer id;

    @NotNull(message = "父级ID不能为空，顶层传0")
    @ApiModelProperty("父级ID,顶层传0")
    private Integer parentId;

    @NotEmpty(message = "类别名称不能为空")
    @ApiModelProperty("类别名称")
    private String name;

    @ApiModelProperty("类别编码")
    private String code;

    @NotEmpty(message = "链接不能为空")
    @ApiModelProperty("列表链接")
    private String listUrl;

    @NotEmpty(message = "链接不能为空")
    @ApiModelProperty("详情页链接")
    private String detailUrl;

    @NotEmpty(message = "链接不能为空")
    @ApiModelProperty("新建链接")
    private String newUrl;

    @ApiModelProperty("显示状态")
    private Integer status;

    @ApiModelProperty("排序")
    private Integer sort;

}
