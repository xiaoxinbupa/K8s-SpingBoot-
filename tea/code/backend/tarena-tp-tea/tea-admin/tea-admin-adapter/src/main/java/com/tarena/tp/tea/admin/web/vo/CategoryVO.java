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
package com.tarena.tp.tea.admin.web.vo;

import com.tarena.tp.tea.admin.dto.CategoryDTO;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
public class CategoryVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("绑定状态")
    private Integer bindStatus;

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

    @ApiModelProperty("父级ID")
    private Integer parentId;

    @ApiModelProperty("类别名称")
    private String name;

    @ApiModelProperty("类别编码")
    private String code;

    @ApiModelProperty("深度")
    private Integer depth;

    @ApiModelProperty("叶子节点")
    private Integer isLeaf;

    @ApiModelProperty("列表链接")
    private String listUrl;

    @ApiModelProperty("详情页链接")
    private String detailUrl;

    @ApiModelProperty("新建链接")
    private String newUrl;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("子类别")
    private List<CategoryDTO> child;
}
