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
package com.tarena.tp.tea.admin.dao;

import com.tarena.tp.tea.admin.query.CategoryQuery;
import com.tarena.tp.tea.po.ContentCategory;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentCategoryMapper {
    int insert(ContentCategory record);

    int insertSelective(ContentCategory record);

    ContentCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ContentCategory record);

    int updateByPrimaryKey(ContentCategory record);

    List<ContentCategory> queryPageListByParam(CategoryQuery query);

    List<ContentCategory> queryListByIdList(@Param("ids") List<Integer> ids);

    List<ContentCategory> queryListByParentIdList(@Param("parentIds") List<Integer> parentIds);

    List<ContentCategory> queryListByParentId(Integer parentId);

    List<ContentCategory> getCategoryListByStatus(Integer bindStatus);

    List<ContentCategory> getAllCategoryList();
}