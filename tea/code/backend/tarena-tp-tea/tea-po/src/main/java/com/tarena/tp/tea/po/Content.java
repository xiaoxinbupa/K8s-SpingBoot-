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
package com.tarena.tp.tea.po;

import lombok.Data;

@Data
public class Content {

    private Integer id;

    private Integer categoryId;

    private String editor;

    private String title;

    private String brief;

    private String keywords;

    private Integer sort;

    private String coverUrl;

    private Integer mediaType;

    private String mediaUrl;

    private Integer up;

    private Integer down;

    private Integer auditStatus;

    private String auditRemark;

    private Integer clickTimes;

    private Integer commentTimes;

    private String ip;

    private Integer status;

    private Long createUserId;

    private Long gmtCreate;

    private Long modifiedUserId;

    private Long gmtModified;

    private String createUserName;

    private String modifiedUserName;
}