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
package com.tarena.tp.tea.server.manager;

import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.common.util.NetworkUtil;
import com.tarena.tp.tea.server.dto.CommentDTO;
import com.tarena.tp.tea.server.dto.ContentDTO;
import com.tarena.tp.tea.server.param.CommentParam;
import com.tarena.tp.tea.server.persists.repository.CommentRepository;
import com.tarena.tp.tea.server.persists.repository.ContentRepository;
import com.tedu.inn.protocol.exception.BusinessException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class CommentService {

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private ContentRepository contentRepository;

    /**
     * 功能描述: 发表文章评论
     */
    public void userComment(HttpServletRequest request, CommentParam commentParam) throws Exception {
        //0.ip获取
        commentParam.setIp(NetworkUtil.getIpAddr(request));
        //1.内容数据检查
        ContentDTO contentDTO = contentRepository.getContentInfoById(commentParam.getContentId());
        if (contentDTO == null) {
            throw new BusinessException(ResultEnum.CONTENT_NOT_EXISTS);
        }
        //2.引用评论检查
        if (!StringUtils.isEmpty(commentParam.getReferenceIds())) {
            CommentDTO commentDTO = commentRepository.getCommentInfoById(Integer.valueOf(commentParam.getReferenceIds()));
            if (commentDTO == null) {
                throw new BusinessException(ResultEnum.COMMENT_NOT_EXISTS);
            }
        } else {
            commentParam.setReferenceIds(0);
        }
        commentRepository.userComment(commentParam);
    }

    /**
     * 功能描述: 评论顶和踩
     */
    public void operateComment(Integer commentId, Integer type) throws Exception {
        commentRepository.operateComment(commentId, type);
    }


}
