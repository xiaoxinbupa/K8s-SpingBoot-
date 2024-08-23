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
package com.tarena.tp.tea.server.dao;

import com.tarena.tp.tea.po.Comment;
import com.tarena.tp.tea.server.query.CommentQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper {
    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    void upComment(@Param("commentId") Integer commentId,@Param("oldCount") Integer oldCount,@Param("newCount") Integer newCount);

    void downComment(@Param("commentId") Integer commentId,@Param("oldCount") Integer oldCount,@Param("newCount") Integer newCount);

    Integer getMaxFloorByParam(@Param("contentId") Integer contentId,@Param("referenceIds") Integer referenceIds);

    List<Comment> getCommentListByCommentQuery(CommentQuery query);

}