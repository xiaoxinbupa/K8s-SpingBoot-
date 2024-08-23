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
public class ContentDTO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("类别ID")
    private Integer categoryId;

    @ApiModelProperty("编辑器类型")
    private String editor;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("摘要")
    private String brief;

    @ApiModelProperty("关键词")
    private String keywords;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("封面图片")
    private String coverUrl;

    @ApiModelProperty("媒体类型")
    private Integer mediaType;

    @ApiModelProperty("媒体url")
    private String mediaUrl;

    @ApiModelProperty("顶次数")
    private Integer up;

    @ApiModelProperty("踩次数")
    private Integer down;

    @ApiModelProperty("审核状态")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("点击次数")
    private Integer clickTimes;

    @ApiModelProperty("评论次数")
    private Integer commentTimes;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("显示状态")
    private Integer status;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private Long gmtCreate;

    @ApiModelProperty("修改人id")
    private Long modifiedUserId;

    @ApiModelProperty("修改时间")
    private Long gmtModified;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("修改人姓名")
    private String modifiedUserName;

    @ApiModelProperty("详情信息")
    private String detail;

    @ApiModelProperty("类别名称")
    private String categoryName;

}