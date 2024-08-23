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
package com.tarena.tp.tea.admin.query;

import com.tedu.inn.protocol.model.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class ContentQuery extends PageRequest implements Serializable {

    @ApiModelProperty("类别ID")
    private Integer categoryId;

    @ApiModelProperty("审核状态[0:待审核;1审核通过;2:审核拒绝]")
    private Integer auditStatus;

    @ApiModelProperty("文章名称")
    private String title;

    @ApiModelProperty("开始时间")
    private Long createTime;

    @ApiModelProperty("结束时间")
    private Long endTime;
}
