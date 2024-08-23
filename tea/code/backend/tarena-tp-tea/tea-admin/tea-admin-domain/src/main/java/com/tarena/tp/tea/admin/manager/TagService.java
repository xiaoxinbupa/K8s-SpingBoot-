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
package com.tarena.tp.tea.admin.manager;

import com.tarena.tp.tea.admin.dto.TagDTO;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.param.TagParam;
import com.tarena.tp.tea.admin.query.TagQuery;
import com.tarena.tp.tea.admin.repository.TagRepository;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TagService {

    @Resource
    private TagRepository tagRepository;

    /**
     * 功能描述: 打标签
     */
    public void saveTagInfo(TagParam tagParam) throws Exception {
        tagRepository.saveTagInfo(tagParam);
    }

    /**
     * 功能描述: 查看同类型标签
     */
    public List<TagDTO> getTagInfoByTagType(Integer contentType) {
        return tagRepository.getTagInfoByTagType(contentType);
    }

    /**
     * 功能描述: 查看标签
     */
    public PageResponse<TagDTO> getTagListByParam(TagQuery tagQuery) {
        return tagRepository.getTagListByParam(tagQuery);
    }

    /**
     * 功能描述: 标签启用
     */
    public void enableTagInfo(EnableStatusParam enableStatusParam) throws Exception {
        tagRepository.updateTagStatus(enableStatusParam);
    }

    /**
     * 功能描述: 查看类型标签[不分页]
     */
    public List<TagDTO> getTagTypeList() {
        return tagRepository.getTagTypeList();
    }

    /**
     * 功能描述: 新增文章-添加标签的二级树
     */
    public Map<String,List<TagDTO>> getTagTree() {
        return tagRepository.getTagTree();
    }

}
