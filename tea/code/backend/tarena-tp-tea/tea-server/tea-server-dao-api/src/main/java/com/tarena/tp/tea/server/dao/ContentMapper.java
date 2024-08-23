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

import com.tarena.tp.tea.po.Content;
import com.tarena.tp.tea.server.query.HotContentQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper {

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Integer id);

    List<Content> getContentListByCategoryId(Integer categoryId);

    List<Content> getMenuContentListByParam(@Param("categoryIds") List<Integer> categoryIds);

    List<Content> getHotContentList(HotContentQuery hotContentQuery);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKey(Content record);

    void auditContent(@Param("contentId") Integer contentId,@Param("auditStatus") Integer auditStatus,@Param("modifiedUserId") Long modifiedUserId,@Param("gmtModified") Long gmtModified,@Param("modifiedUserName") String modifiedUserName);

    void upContent(@Param("contentId") Integer contentId,@Param("oldCount") Integer oldCount,@Param("newCount") Integer newCount);

    void downContent(@Param("contentId") Integer contentId,@Param("oldCount") Integer oldCount,@Param("newCount") Integer newCount);

    void clickContent(@Param("contentId") Integer contentId,@Param("oldCount") Integer oldCount,@Param("newCount") Integer newCount);

    void commentContent(@Param("contentId") Integer contentId,@Param("oldCount") Integer oldCount,@Param("newCount") Integer newCount);
}