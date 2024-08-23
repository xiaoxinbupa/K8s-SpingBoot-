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

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.tarena.tp.tea.admin.converter.CategoryConverter;
import com.tarena.tp.tea.admin.dao.ContentCategoryMapper;
import com.tarena.tp.tea.admin.dto.CategoryDTO;
import com.tarena.tp.tea.admin.param.CategoryParam;
import com.tarena.tp.tea.admin.query.CategoryQuery;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.po.ContentCategory;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class CategoryRepositoryImpl implements CategoryRepository {

    @Resource
    private CategoryConverter categoryConverter;

    @Resource
    private ContentCategoryMapper categoryMapper;

    @Resource
    private MenuRepository menuRepository;

    @Override
    public void saveCategoryInfo(CategoryParam param) throws Exception {
        ContentCategory parentContentCategory = new ContentCategory();
        if (param.getParentId() != 0) {
            parentContentCategory = categoryMapper.selectByPrimaryKey(param.getParentId());
            if (parentContentCategory == null) {
                throw new BusinessException(ResultEnum.CATEGORY_NOT_EXISTS);
            }
        }
        ContentCategory contentCategory = categoryConverter.convertParamToModel(param);
        contentCategory.setDepth(param.getParentId() == 0 ? 1 : parentContentCategory.getDepth() + 1);
        contentCategory.setIsLeaf(contentCategory.getDepth() == 3 ? 1 : 0);
        if (contentCategory.getId() == null) {
            categoryMapper.insert(contentCategory);
        } else {
            menuRepository.updateMenuUrl(param.getId(), param.getCode());
            categoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
    }

    @Override
    public PageResponse<CategoryDTO> queryCategoryList(CategoryQuery query) {
        PageMethod.startPage(query.getPageNo(), query.getPageSize());
        List<ContentCategory> categoryList = categoryMapper.queryPageListByParam(query);
        PageInfo<ContentCategory> page = new PageInfo<>(categoryList);
        List<CategoryDTO> contentDtoList = categoryConverter.convertModelListToDtoList(page.getList());
        return new PageResponse<>(query, page.getTotal(), contentDtoList);
    }

    @Override
    public CategoryDTO queryCategoryById(Integer id) {
        ContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
        return categoryConverter.convertModelToDto(contentCategory);
    }

    @Override
    public List<CategoryDTO> getCategoryListByStatus(Integer status) {
        List<ContentCategory> contentCategory = categoryMapper.getCategoryListByStatus(status);
        return categoryConverter.convertModelListToDtoList(contentCategory);
    }

    @Override
    public List<CategoryDTO> queryListByIdList(List<Integer> ids) {
        List<ContentCategory> contentCategoryList = categoryMapper.queryListByIdList(ids);
        return categoryConverter.convertModelListToDtoList(contentCategoryList);
    }

    @Override
    public List<CategoryDTO> queryListByParentId(Integer parentId) {
        List<ContentCategory> contentCategoryList = categoryMapper.queryListByParentId(parentId);
        return categoryConverter.convertModelListToDtoList(contentCategoryList);
    }

    @Override
    public void bindCategory(Integer id, Integer bindStatus) {
        ContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
        contentCategory.setBindStatus(bindStatus);
        categoryMapper.updateByPrimaryKeySelective(contentCategory);
    }

    @Override
    public Map<Integer, List<CategoryDTO>> getAllCategoryList() {
        List<ContentCategory> contentCategoryList = categoryMapper.getAllCategoryList();
        List<CategoryDTO> contentCategoryDtoList = categoryConverter.convertModelListToDtoList(contentCategoryList);
        return contentCategoryDtoList.stream().collect(Collectors.groupingBy(CategoryDTO::getParentId));
    }

    @Override
    public List<CategoryDTO> queryCategoryTree() {
        List<CategoryDTO> result = new ArrayList<>();
        Map<Integer, List<CategoryDTO>> allCategoryMap = getAllCategoryList();
        if (CollectionUtils.isEmpty(allCategoryMap)) {
            return result;
        }
        List<CategoryDTO> firstCategoryList = allCategoryMap.get(0);
        if (CollectionUtils.isEmpty(firstCategoryList)) {
            return result;
        }
        for (CategoryDTO categoryDTO : firstCategoryList) {
            result.add(categoryDTO);
            //获取二级分类
            List<CategoryDTO> secondCategoryList = allCategoryMap.get(categoryDTO.getId());
            if (CollectionUtils.isEmpty(secondCategoryList)) {
                continue;
            }
            categoryDTO.setChild(secondCategoryList);
            //获取三级分类
            for (CategoryDTO secondCategory : categoryDTO.getChild()) {
                List<CategoryDTO> thirdAreaList = allCategoryMap.get(secondCategory.getId());
                secondCategory.setChild(thirdAreaList);
            }
        }
        return result;
    }
}
