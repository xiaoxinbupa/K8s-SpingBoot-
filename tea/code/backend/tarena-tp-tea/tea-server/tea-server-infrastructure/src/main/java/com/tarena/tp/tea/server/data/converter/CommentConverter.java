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
package com.tarena.tp.tea.server.data.converter;

import com.tarena.passport.protocol.LoginUser;
import com.tarena.passport.sdk.context.SecurityContext;
import com.tarena.tp.tea.common.enums.AuditStatusEnum;
import com.tarena.tp.tea.common.enums.EnableTypeEnum;
import com.tarena.tp.tea.po.Comment;
import com.tarena.tp.tea.server.dto.CommentDTO;
import com.tarena.tp.tea.server.param.CommentParam;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class CommentConverter {

    public CommentDTO convertModelToDto(Comment source) {
        if (source == null) {
            return null;
        }
        CommentDTO target = new CommentDTO();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public Comment convertParamToModel(CommentParam source) {
        if (source == null) {
            return null;
        }
        Comment target = new Comment();
        BeanUtils.copyProperties(source,target);
        LoginUser loginUser = SecurityContext.getLoginToken();
        if (loginUser != null) {
            target.setCreateUserId(loginUser.getUserId());
            target.setCreateUserName(loginUser.getUserName());
            target.setGmtCreate(System.currentTimeMillis());
            target.setModifiedUserId(loginUser.getUserId());
            target.setModifiedUserName(loginUser.getUserName());
            target.setGmtModified(System.currentTimeMillis());
        }
        target.setUp(0);
        target.setDown(0);
        target.setAuditStatus(AuditStatusEnum.PASS.getType());
        target.setStatus(EnableTypeEnum.ON.getType());
        return target;
    }


    public List<CommentDTO> convertModelListToDtoList(List<Comment> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<CommentDTO> targets = new ArrayList<>();
        for (Comment source : sources) {
            CommentDTO target = new CommentDTO();
            BeanUtils.copyProperties(source,target);
            targets.add(target);
        }
        return targets;
    }
}
