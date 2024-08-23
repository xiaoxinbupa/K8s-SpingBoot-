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
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContentParam {

    @ApiModelProperty("id")
    private Integer id;

    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty("类别ID")
    private Integer categoryId;

    @NotEmpty(message = "编辑器类型不能为空")
    @ApiModelProperty("编辑器类型")
    private String editor;

    @NotEmpty(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @ApiModelProperty("摘要")
    private String brief;

    @NotNull(message = "标签id不能为空")
    @ApiModelProperty("标签id")
    private List<Integer> tagIds;

    @ApiModelProperty("关键词[前端忽略]")
    private String keywords;


    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("封面图片")
    private String coverUrl;

    @ApiModelProperty("ip")
    private String ip;

    @NotEmpty(message = "内容详情不能为空")
    @ApiModelProperty("内容详情")
    private String detail;

}
