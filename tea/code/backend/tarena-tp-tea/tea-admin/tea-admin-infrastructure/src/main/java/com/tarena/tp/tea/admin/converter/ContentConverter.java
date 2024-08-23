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
package com.tarena.tp.tea.admin.converter;

import com.tarena.passport.protocol.LoginUser;
import com.tarena.passport.sdk.context.SecurityContext;
import com.tarena.tp.tea.admin.dto.ContentDTO;
import com.tarena.tp.tea.admin.param.ContentParam;
import com.tarena.tp.tea.common.enums.AuditStatusEnum;
import com.tarena.tp.tea.common.enums.EnableTypeEnum;
import com.tarena.tp.tea.po.Content;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class ContentConverter {

    public ContentDTO convertModelToDto(Content source) {
        if (source == null) {
            return null;
        }
        ContentDTO target = new ContentDTO();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public Content convertParamToModel(ContentParam source) {
        if (source == null) {
            return null;
        }
        Content content = new Content();
        BeanUtils.copyProperties(source,content);
        content.setGmtCreate(System.currentTimeMillis());
        content.setStatus(EnableTypeEnum.OFF.getType());
        content.setAuditStatus(AuditStatusEnum.NOT_APPROVED.getType());
        content.setUp(0);
        content.setDown(0);
        content.setClickTimes(0);
        content.setCommentTimes(0);
        content.setSort(source.getSort() == null ? 0 : source.getSort());
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (loginUser != null) {
            if (source.getId() == null) {
                content.setCreateUserId(loginUser.getUserId());
                content.setCreateUserName(loginUser.getUserName());
                content.setGmtCreate(System.currentTimeMillis());
                content.setModifiedUserId(loginUser.getUserId());
                content.setModifiedUserName(loginUser.getUserName());
                content.setGmtModified(System.currentTimeMillis());
            } else {
                content.setModifiedUserId(loginUser.getUserId());
                content.setModifiedUserName(loginUser.getUserName());
                content.setGmtModified(System.currentTimeMillis());
            }
        }
        return content;
    }


    public List<ContentDTO> convertModelListToDtoList(List<Content> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<ContentDTO> targets = new ArrayList<>();
        for (Content source : sources) {
            ContentDTO target = new ContentDTO();
            BeanUtils.copyProperties(source,target);
            targets.add(target);
        }
        return targets;
    }
}
