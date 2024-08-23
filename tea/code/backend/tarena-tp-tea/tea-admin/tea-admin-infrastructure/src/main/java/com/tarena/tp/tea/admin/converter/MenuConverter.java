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
import com.tarena.tp.tea.admin.dto.MenuDTO;
import com.tarena.tp.tea.admin.param.MenuParam;
import com.tarena.tp.tea.common.enums.AuditStatusEnum;
import com.tarena.tp.tea.common.enums.EnableTypeEnum;
import com.tarena.tp.tea.po.Menu;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class MenuConverter {

    public MenuDTO convertModelToDto(Menu source) {
        if (source == null) {
            return null;
        }
        MenuDTO target = new MenuDTO();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public Menu convertParamToModel(MenuParam source) {
        if (source == null) {
            return null;
        }
        Menu target = new Menu();
        BeanUtils.copyProperties(source,target);
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (source.getId() == null) {
            if (loginUser != null) {
                target.setCreateUserId(loginUser.getUserId());
                target.setCreateUserName(loginUser.getUserName());
                target.setGmtCreate(System.currentTimeMillis());
                target.setModifiedUserId(loginUser.getUserId());
                target.setModifiedUserName(loginUser.getUserName());
                target.setGmtModified(System.currentTimeMillis());
            }
        } else {
            if (loginUser != null) {
                target.setModifiedUserId(loginUser.getUserId());
                target.setModifiedUserName(loginUser.getUserName());
                target.setGmtModified(System.currentTimeMillis());
            }
        }
        target.setSort(source.getSort() == null ? 0 : source.getSort());
        target.setIcon(source.getIcon() == null ? "" : source.getIcon());
        target.setAuditStatus(AuditStatusEnum.PASS.getType());
        target.setStatus(source.getStatus() == null ? EnableTypeEnum.ON.getType() : source.getStatus());
        return target;
    }


    public List<MenuDTO> convertModelListToDtoList(List<Menu> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<MenuDTO> targets = new ArrayList<>();
        for (Menu source : sources) {
            MenuDTO target = new MenuDTO();
            BeanUtils.copyProperties(source,target);
            targets.add(target);
        }
        return targets;
    }

    public Menu convertToModel(Menu source,String url) {
        Menu target = new Menu();
        BeanUtils.copyProperties(source,target);
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (loginUser != null) {
            target.setModifiedUserId(loginUser.getUserId());
            target.setModifiedUserName(loginUser.getUserName());
            target.setGmtModified(System.currentTimeMillis());
        }
        target.setUrl(url);
        return target;
    }
}
