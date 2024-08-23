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
import com.tarena.tp.tea.admin.dto.DetailDTO;
import com.tarena.tp.tea.admin.param.ContentParam;
import com.tarena.tp.tea.po.ContentDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContentDetailConverter {

    public DetailDTO convertModelToDto(ContentDetail source) {
        if (source == null) {
            return null;
        }
        DetailDTO target = new DetailDTO();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public ContentDetail convertParamToModel(ContentParam source) {
        if (source == null) {
            return null;
        }
        ContentDetail contentDetail = new ContentDetail();
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (loginUser != null) {
            if (source.getId() == null) {
                contentDetail.setCreateUserId(loginUser.getUserId());
                contentDetail.setCreateUserName(loginUser.getUserName());
                contentDetail.setGmtCreate(System.currentTimeMillis());
                contentDetail.setModifiedUserId(loginUser.getUserId());
                contentDetail.setModifiedUserName(loginUser.getUserName());
                contentDetail.setGmtModified(System.currentTimeMillis());
            } else {
                contentDetail.setModifiedUserId(loginUser.getUserId());
                contentDetail.setModifiedUserName(loginUser.getUserName());
                contentDetail.setGmtModified(System.currentTimeMillis());
            }
        }
        contentDetail.setDetail(source.getDetail());
        return contentDetail;
    }

}
