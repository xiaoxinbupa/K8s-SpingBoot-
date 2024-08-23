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
package com.tarena.tp.tea.admin.repository;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.tarena.tp.tea.admin.converter.TagConverter;
import com.tarena.tp.tea.admin.dao.TagMapper;
import com.tarena.tp.tea.admin.dto.TagDTO;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.param.TagParam;
import com.tarena.tp.tea.admin.query.TagQuery;
import com.tarena.tp.tea.po.Tag;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    public void saveTagInfo(TagParam tagParam) {
        Tag tag = tagConverter.convertParamToModel(tagParam);
        if (tagParam.getId() == null) {
            tagMapper.insert(tag);
        } else {
            tagMapper.updateByPrimaryKeySelective(tag);
        }
    }

    @Override
    public List<TagDTO> getTagInfoByTagType(Integer tagType) {
        List<Tag> tagList = tagMapper.getTagInfoByTagType(tagType);
        return tagConverter.convertModelListToDtoList(tagList);
    }

    @Override
    public String getTagKeywordsByIdList(List<Integer> ids) {
        List<Tag> tagList = tagMapper.getTagByIdList(ids);
        List<TagParam> tagParamList = new ArrayList<>();
        for (Tag tag : tagList) {
            TagParam tagParam = new TagParam();
            tagParam.setTagType(tag.getTagType());
            tagParam.setId(tag.getId());
            tagParam.setName(tag.getName());
            tagParamList.add(tagParam);
        }
        return JSONObject.toJSONString(tagParamList);
    }

//    @Override
//    public String getTagKeywordsByIdList(List<Integer> ids) throws Exception {
//        String keyWords = "";
//        List<Tag> tagList = tagMapper.getTagByIdList(ids);
//        if (CollectionUtils.isEmpty(tagList) || tagList.size() != ids.size()) {
//            throw new BusinessException(ResultEnum.TAG_NO_EXISTS);
//        }
//        for (Tag tag : tagList) {
//            keyWords = tag.getName() + ",";
//        }
//        return keyWords.substring(keyWords.length() - 1);
//    }

    @Override
    public PageResponse<TagDTO> getTagListByParam(TagQuery query) {
        PageMethod.startPage(query.getPageNo(), query.getPageSize());
        List<Tag> tagList = tagMapper.getTagListByParam(query);
        PageInfo<Tag> page = new PageInfo<>(tagList);
        List<TagDTO> contentDtoList = tagConverter.convertModelListToDtoList(page.getList());
        return new PageResponse<>(query, page.getTotal(), contentDtoList);
    }

    @Override
    public void updateTagStatus(EnableStatusParam enableStatusParam) {
        tagMapper.updateStatusByIdList(enableStatusParam.getStatus(), enableStatusParam.getIds());
    }

    @Override
    public List<TagDTO> getTagTypeList() {
        List<Tag> tagList = tagMapper.getTagInfoByTagType(0);
        return tagConverter.convertModelListToDtoList(tagList);
    }

    @Override
    public Map<String,List<TagDTO>> getTagTree() {
        Map<String,List<TagDTO>> map = new HashMap<>();
        List<Tag> allTagList = tagMapper.queryAllTagList();
        List<Tag> tagTypeList = allTagList.stream().filter(e -> e.getTagType().equals(0)).collect(Collectors.toList());
        for (Tag tag : tagTypeList) {
            List<Tag> tagList = allTagList.stream().filter(e -> e.getTagType().equals(tag.getId())).collect(Collectors.toList());
            map.put(tag.getName(),tagConverter.convertModelListToDtoList(tagList));
        }
        return map;
    }
}
