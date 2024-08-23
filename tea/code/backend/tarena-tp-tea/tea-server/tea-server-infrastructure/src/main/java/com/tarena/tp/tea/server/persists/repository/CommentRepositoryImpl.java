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
package com.tarena.tp.tea.server.persists.repository;

import com.tarena.tp.tea.common.enums.ContentOperateTypeEnum;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.po.Comment;
import com.tarena.tp.tea.po.Content;
import com.tarena.tp.tea.server.dao.CommentMapper;
import com.tarena.tp.tea.server.dao.ContentMapper;
import com.tarena.tp.tea.server.data.converter.CommentConverter;
import com.tarena.tp.tea.server.dto.CommentDTO;
import com.tarena.tp.tea.server.param.CommentParam;
import com.tarena.tp.tea.server.query.CommentQuery;
import com.tedu.inn.protocol.exception.BusinessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class CommentRepositoryImpl implements CommentRepository {

    @Resource
    private CommentConverter commentConverter;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ContentMapper contentMapper;

    @Override
    public CommentDTO getCommentInfoById(Integer id) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        return commentConverter.convertModelToDto(comment);
    }

    @Override
    public void userComment(CommentParam commentParam) throws Exception {
        Content content = contentMapper.selectByPrimaryKey(commentParam.getContentId());
        //3.评论数据插入
        Comment comment = commentConverter.convertParamToModel(commentParam);
        //4.查询最大楼层
        Integer floor = commentMapper.getMaxFloorByParam(commentParam.getContentId(), commentParam.getReferenceIds());
        comment.setFloor(floor == null ? 1 : floor + 1);
        commentMapper.insert(comment);
        //5.评论次数增加
        contentMapper.commentContent(content.getId(), content.getCommentTimes(), content.getCommentTimes() + 1);
    }

    @Override
    public Map<Integer,List<CommentDTO>> getCommentListByCommentQuery(CommentQuery query) {
        Map<Integer,List<CommentDTO>> map = new HashMap<>();
        List<Comment> commentList = commentMapper.getCommentListByCommentQuery(query);
        if (CollectionUtils.isEmpty(commentList)) {
            return map;
        }
        List<CommentDTO> commentDtoList = commentConverter.convertModelListToDtoList(commentList);
        return commentDtoList.stream().collect(Collectors.groupingBy(CommentDTO::getReferenceIds));
    }

    @Override
    public void operateComment(Integer commentId, Integer type) throws Exception {
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            throw new BusinessException(ResultEnum.COMMENT_NOT_EXISTS);
        }
        if (ContentOperateTypeEnum.UP.getType().equals(type)) {
            commentMapper.upComment(comment.getId(), comment.getUp(), comment.getUp() + 1);
        } else if (ContentOperateTypeEnum.DOWN.getType().equals(type)) {
            commentMapper.downComment(comment.getId(), comment.getDown(), comment.getDown() + 1);
        }
    }
}
