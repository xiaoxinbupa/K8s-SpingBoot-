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
import com.tarena.tp.tea.admin.dto.CategoryDTO;
import com.tarena.tp.tea.admin.param.CategoryParam;
import com.tarena.tp.tea.common.enums.BindStatusEnum;
import com.tarena.tp.tea.po.ContentCategory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class CategoryConverter {

    public CategoryDTO convertModelToDto(ContentCategory source) {
        if (source == null) {
            return null;
        }
        CategoryDTO target = new CategoryDTO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public ContentCategory convertParamToModel(CategoryParam source) {
        if (source == null) {
            return null;
        }
        ContentCategory target = new ContentCategory();
        BeanUtils.copyProperties(source, target);
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (source.getId() == null) {
            target.setCreateUserId(loginUser.getUserId());
            target.setCreateUserName(loginUser.getUserName());
            target.setGmtCreate(System.currentTimeMillis());
            target.setModifiedUserId(loginUser.getUserId());
            target.setModifiedUserName(loginUser.getUserName());
            target.setGmtModified(System.currentTimeMillis());
            target.setBindStatus(BindStatusEnum.UNBIND.getType());
        } else {
            target.setModifiedUserId(loginUser.getUserId());
            target.setModifiedUserName(loginUser.getUserName());
            target.setGmtModified(System.currentTimeMillis());
        }
        target.setSort(source.getSort() == null ? 0 : source.getSort());
        return target;
    }

    public List<CategoryDTO> convertModelListToDtoList(List<ContentCategory> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<CategoryDTO> targets = new ArrayList<>();
        for (ContentCategory source : sources) {
            CategoryDTO target = new CategoryDTO();
            BeanUtils.copyProperties(source, target);
            targets.add(target);
        }
        return targets;
    }
}
