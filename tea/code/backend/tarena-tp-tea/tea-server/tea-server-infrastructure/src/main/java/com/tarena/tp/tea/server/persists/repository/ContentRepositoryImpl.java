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

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.tarena.tp.tea.common.enums.ContentOperateTypeEnum;
import com.tarena.tp.tea.common.enums.MenuTypeEnum;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.po.Content;
import com.tarena.tp.tea.server.dao.ContentMapper;
import com.tarena.tp.tea.server.data.converter.ContentConverter;
import com.tarena.tp.tea.server.dto.CategoryDTO;
import com.tarena.tp.tea.server.dto.ContentDTO;
import com.tarena.tp.tea.server.dto.MenuDTO;
import com.tarena.tp.tea.server.manager.CategoryService;
import com.tarena.tp.tea.server.query.ContentQuery;
import com.tarena.tp.tea.server.query.HotContentQuery;
import com.tarena.tp.tea.server.query.MenuContentQuery;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class ContentRepositoryImpl implements ContentRepository {

    @Resource
    private ContentConverter contentConverter;

    @Resource
    private ContentMapper contentMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public PageResponse<ContentDTO> getContentListByParam(ContentQuery query) {
        CategoryDTO categoryDto = categoryService.queryCategoryByCode(query.getCategoryCode());
        PageMethod.startPage(query.getPageNo(), query.getPageSize());
        List<Content> contentList = contentMapper.getContentListByCategoryId(categoryDto.getId());
        PageInfo<Content> page = new PageInfo<>(contentList);
        List<ContentDTO> contentDtoList = contentConverter.convertModelListToDtoList(page.getList());
        makeContentDto(contentDtoList, Lists.newArrayList(categoryDto));
        return new PageResponse<>(query, page.getTotal(), contentDtoList);
    }

    @Override
    public PageResponse<ContentDTO> getMenuContentListByParam(MenuContentQuery query) throws Exception {
        //0.外部链接前端不调用list，调用后返回为空
        List<ContentDTO> contentDtoList = new ArrayList<>();
        MenuDTO menuDTO = menuRepository.queryMenuById(query.getMenuId());
        if (MenuTypeEnum.OUT.getType().equals(menuDTO.getType())) {
            return new PageResponse<>(query, 0L, contentDtoList);
        }
        //1.查询菜单下的类别
        List<CategoryDTO> categoryList = categoryService.queryCategoryList(query.getMenuId());
        //5、查询内容
        List<Integer> categoryIdList = categoryList.stream().map(e -> e.getId()).collect(Collectors.toList());
        PageMethod.startPage(query.getPageNo(), query.getPageSize());
        List<Content> contentList = contentMapper.getMenuContentListByParam(categoryIdList);
        PageInfo<Content> page = new PageInfo<>(contentList);
        contentDtoList = contentConverter.convertModelListToDtoList(page.getList());
        makeContentDto(contentDtoList, categoryList);
        return new PageResponse<>(query, page.getTotal(), contentDtoList);
    }

    @Override
    public List<ContentDTO> getHotContentList(HotContentQuery hotContentQuery) {
        List<Content> contentList = contentMapper.getHotContentList(hotContentQuery);
        return contentConverter.convertModelListToDtoList(contentList);
    }

    public void makeContentDto(List<ContentDTO> contentDtoList, List<CategoryDTO> categoryList) {
        if (!CollectionUtils.isEmpty(contentDtoList)) {
            Map<Integer, String> categoryMap = categoryList.stream().collect(Collectors.toMap(CategoryDTO::getId, CategoryDTO::getName));
            //封装实体中的类别
            for (ContentDTO contentDTO : contentDtoList) {
                contentDTO.setCategoryName(categoryMap.get(contentDTO.getCategoryId()));
            }
        }
    }

    @Override
    public void operateContent(Integer contentId, Integer type) throws Exception {
        Content content = contentMapper.selectByPrimaryKey(contentId);
        if (content == null) {
            throw new BusinessException(ResultEnum.CONTENT_NOT_EXISTS);
        }
        if (ContentOperateTypeEnum.UP.getType().equals(type)) {
            contentMapper.upContent(content.getId(), content.getUp(), content.getUp() + 1);
        } else if (ContentOperateTypeEnum.DOWN.getType().equals(type)) {
            contentMapper.downContent(content.getId(), content.getDown(), content.getDown() + 1);
        } else {
            contentMapper.clickContent(content.getId(), content.getClickTimes(), content.getClickTimes() + 1);
        }
    }

    @Override
    public ContentDTO getContentInfoById(Integer contentId) {
        Content content = contentMapper.selectByPrimaryKey(contentId);
        return contentConverter.convertModelToDto(content);
    }
}
