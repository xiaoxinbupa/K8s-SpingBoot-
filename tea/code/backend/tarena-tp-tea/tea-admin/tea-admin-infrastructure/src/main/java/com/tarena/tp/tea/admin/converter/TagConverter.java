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
import com.tarena.tp.tea.admin.dto.TagDTO;
import com.tarena.tp.tea.admin.param.TagParam;
import com.tarena.tp.tea.common.enums.EnableTypeEnum;
import com.tarena.tp.tea.po.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class TagConverter {

    public TagDTO convertModelToDto(Tag source) {
        if (source == null) {
            return null;
        }
        TagDTO target = new TagDTO();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public Tag convertParamToModel(TagParam source) {
        if (source == null) {
            return null;
        }
        Tag target = new Tag();
        BeanUtils.copyProperties(source,target);
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (source.getId() == null) {
            target.setCreateUserId(loginUser.getUserId());
            target.setCreateUserName(loginUser.getUserName());
            target.setGmtCreate(System.currentTimeMillis());
            target.setModifiedUserId(loginUser.getUserId());
            target.setModifiedUserName(loginUser.getUserName());
            target.setGmtModified(System.currentTimeMillis());
        } else {
            target.setModifiedUserId(loginUser.getUserId());
            target.setModifiedUserName(loginUser.getUserName());
            target.setGmtModified(System.currentTimeMillis());
        }
        target.setStatus(EnableTypeEnum.ON.getType());
        return target;
    }


    public List<TagDTO> convertModelListToDtoList(List<Tag> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<TagDTO> targets = new ArrayList<>();
        for (Tag source : sources) {
            TagDTO target = new TagDTO();
            BeanUtils.copyProperties(source,target);
            targets.add(target);
        }
        return targets;
    }
}
