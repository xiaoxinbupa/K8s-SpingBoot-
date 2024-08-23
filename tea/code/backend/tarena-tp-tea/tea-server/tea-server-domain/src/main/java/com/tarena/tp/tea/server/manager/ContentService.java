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

import com.tarena.tp.tea.server.dto.CommentDTO;
import com.tarena.tp.tea.server.dto.ContentDTO;
import com.tarena.tp.tea.server.dto.ContentDetailDTO;
import com.tarena.tp.tea.server.dto.DetailDTO;
import com.tarena.tp.tea.server.persists.repository.CommentRepository;
import com.tarena.tp.tea.server.persists.repository.ContentDetailRepository;
import com.tarena.tp.tea.server.persists.repository.ContentRepository;
import com.tarena.tp.tea.server.query.CommentQuery;
import com.tarena.tp.tea.server.query.ContentQuery;
import com.tarena.tp.tea.server.query.HotContentQuery;
import com.tarena.tp.tea.server.query.MenuContentQuery;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class ContentService {

    @Resource
    private ContentRepository contentRepository;

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private ContentDetailRepository contentDetailRepository;

    /**
     * 功能描述: 内容顶和踩
     */
    public void operateContent(Integer contentId, Integer type) throws Exception {
        contentRepository.operateContent(contentId, type);
    }

    /**
     * 功能描述: 查看文章列表
     */
    public PageResponse<ContentDTO> getContentListByParam(ContentQuery contentQuery) {
        return contentRepository.getContentListByParam(contentQuery);
    }

    /**
     * 功能描述: 查看菜单下的文章列表
     */
    public PageResponse<ContentDTO> getMenuContentListByParam(MenuContentQuery menuContentQuery) throws Exception {
        return contentRepository.getMenuContentListByParam(menuContentQuery);
    }

    /**
     * 功能描述: 查看热门文章列表
     */
    public List<ContentDTO> getHotContentList(HotContentQuery hotContentQuery) {
        return contentRepository.getHotContentList(hotContentQuery);
    }

    private void makeCommentList(CommentDTO commentDTO, Map<Integer, List<CommentDTO>> allCommentMap) {
        List<CommentDTO> reCommentList = allCommentMap.get(commentDTO.getId());
        if (CollectionUtils.isEmpty(reCommentList)) {
            return;
        }
        commentDTO.setReCommentList(reCommentList);
        for (CommentDTO reComment : reCommentList) {
            makeCommentList(reComment, allCommentMap);
        }
    }

    /**
     * 功能描述: 查看文章和评论详情
     */
    public ContentDetailDTO getContentInfoById(CommentQuery query) {
        ContentDetailDTO contentDetailDTO = new ContentDetailDTO();
        List<CommentDTO> commentDtoList = new ArrayList<>();
        Map<Integer, List<CommentDTO>> allCommentMap = commentRepository.getCommentListByCommentQuery(query);
        if (!CollectionUtils.isEmpty(allCommentMap)) {
            //获取一级评论
            commentDtoList = allCommentMap.get(0);
            for (CommentDTO commentDTO : commentDtoList) {
                makeCommentList(commentDTO,allCommentMap);
            }
        }
        ContentDTO contentDTO = contentRepository.getContentInfoById(query.getContentId());
        DetailDTO detailDTO = contentDetailRepository.getDetailInfoByContentId(query.getContentId());
        contentDTO.setDetail(detailDTO == null ? "" : detailDTO.getDetail());
        contentDetailDTO.setContentDTO(contentDTO);
        contentDetailDTO.setCommentList(commentDtoList);
        return contentDetailDTO;
    }

}
