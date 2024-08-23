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

import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.po.ContentCategory;
import com.tarena.tp.tea.po.Menu;
import com.tarena.tp.tea.server.dao.ContentCategoryMapper;
import com.tarena.tp.tea.server.dao.MenuMapper;
import com.tarena.tp.tea.server.data.converter.CategoryConverter;
import com.tarena.tp.tea.server.dto.CategoryDTO;
import com.tedu.inn.protocol.exception.BusinessException;
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
    private MenuMapper menuMapper;

    @Override
    public CategoryDTO queryCategoryById(Integer id) {
        ContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
        return categoryConverter.convertModelToDto(contentCategory);
    }

    @Override
    public CategoryDTO queryCategoryByCode(String code) {
        ContentCategory contentCategory = categoryMapper.selectCategoryByCode(code);
        return categoryConverter.convertModelToDto(contentCategory);
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
    public Map<Integer,List<CategoryDTO>> queryAllCategoryList() {
        List<ContentCategory> contentCategoryList = categoryMapper.queryAllCategoryList();
        List<CategoryDTO> categoryDtoList =  categoryConverter.convertModelListToDtoList(contentCategoryList);
        return categoryDtoList.stream().collect(Collectors.groupingBy(CategoryDTO::getParentId));
    }

    @Override
    public CategoryDTO queryMenuCategoryTree(Integer menuId) throws Exception {
        //1、校验菜单
        Menu menu = menuMapper.selectByPrimaryKey(menuId);
        if (menu == null) {
            throw new BusinessException(ResultEnum.MENU_NOT_EXISTS);
        }
        //2、查询一级类别
        CategoryDTO parentCategory = queryCategoryById(menu.getCategoryId());
        if (parentCategory == null) {
            throw new BusinessException(ResultEnum.CATEGORY_NOT_EXISTS);
        }
        //3、查询二级类别
        Map<Integer,List<CategoryDTO>> allCategoryMap = queryAllCategoryList();
        List<CategoryDTO> secondCategoryList = allCategoryMap.get(parentCategory.getId());
        if (!CollectionUtils.isEmpty(secondCategoryList)) {
            parentCategory.setChild(secondCategoryList);
            //4、查询三级类别
            for (CategoryDTO secondCategory : secondCategoryList) {
                List<CategoryDTO> thirdCategoryList = allCategoryMap.get(secondCategory.getId());
                if (!CollectionUtils.isEmpty(thirdCategoryList)) {
                    secondCategory.setChild(thirdCategoryList);
                }
            }
        }
        return parentCategory;
    }

    @Override
    public List<CategoryDTO> queryCategoryList(Integer menuId) throws Exception {
        //1、校验菜单
        Menu menu = menuMapper.selectByPrimaryKey(menuId);
        if (menu == null) {
            throw new BusinessException(ResultEnum.MENU_NOT_EXISTS);
        }
        List<CategoryDTO> categoryList = new ArrayList<>();
        //2、查询一级类别
        CategoryDTO parentCategory = queryCategoryById(menu.getCategoryId());
        if (parentCategory == null) {
            throw new BusinessException(ResultEnum.CATEGORY_NOT_EXISTS);
        }
        categoryList.add(parentCategory);
        //3、查询二级类别
        Map<Integer,List<CategoryDTO>> allCategoryMap = queryAllCategoryList();
        List<CategoryDTO> secondCategoryList = allCategoryMap.get(menu.getCategoryId());
        if (!CollectionUtils.isEmpty(secondCategoryList)) {
            categoryList.addAll(secondCategoryList);
            //4、查询三级类别
            for (CategoryDTO secondCategory : secondCategoryList) {
                List<CategoryDTO> thirdCategoryList = allCategoryMap.get(secondCategory.getId());
                if (!CollectionUtils.isEmpty(thirdCategoryList)) {
                    categoryList.addAll(thirdCategoryList);
                }
            }
        }
        return categoryList;
    }
}
