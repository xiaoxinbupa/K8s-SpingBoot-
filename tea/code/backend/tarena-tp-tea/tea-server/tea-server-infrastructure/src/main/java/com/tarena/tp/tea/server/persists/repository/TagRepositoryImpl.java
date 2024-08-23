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

import com.tarena.tp.tea.po.Tag;
import com.tarena.tp.tea.server.dao.TagMapper;
import com.tarena.tp.tea.server.data.converter.TagConverter;
import com.tarena.tp.tea.server.dto.TagDTO;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TagRepositoryImpl implements TagRepository {

    @Resource
    private TagConverter tagConverter;

    @Resource
    private TagMapper tagMapper;

    @Override
    public List<TagDTO> getTagInfoByTagType(Integer tagType) {
        List<Tag> tagList = tagMapper.getTagInfoByTagType(tagType);
        return tagConverter.convertModelListToDtoList(tagList);
    }
}
