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
package com.tarena.tp.tea.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuDTO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("一级类别id")
    private Integer categoryId;

    @ApiModelProperty("一级类别名称")
    private String categoryName;

    @ApiModelProperty("类型1内部链接2外部链接")
    private Integer type;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private Long gmtCreate;

    @ApiModelProperty("修改人id")
    private Long modifiedUserId;

    @ApiModelProperty("修改时间")
    private Long gmtModified;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("修改人姓名")
    private String modifiedUserName;

    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("审核状态")
    private Integer auditStatus;

    @ApiModelProperty("文件ID")
    private Integer fileId;

}