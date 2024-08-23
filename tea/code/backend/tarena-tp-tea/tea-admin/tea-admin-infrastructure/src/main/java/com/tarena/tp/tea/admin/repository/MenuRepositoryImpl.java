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
import com.tarena.tp.tea.admin.converter.MenuConverter;
import com.tarena.tp.tea.admin.dao.ContentCategoryMapper;
import com.tarena.tp.tea.admin.dao.MenuMapper;
import com.tarena.tp.tea.admin.dto.CategoryDTO;
import com.tarena.tp.tea.admin.dto.MenuDTO;
import com.tarena.tp.tea.admin.param.MenuParam;
import com.tarena.tp.tea.admin.query.MenuQuery;
import com.tarena.tp.tea.common.enums.BindStatusEnum;
import com.tarena.tp.tea.common.enums.MenuTypeEnum;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.po.ContentCategory;
import com.tarena.tp.tea.po.Menu;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MenuRepositoryImpl implements MenuRepository {

    @Resource
    private MenuConverter menuConverter;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private ContentCategoryMapper categoryMapper;

    @Resource
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu insertMenuInfo(MenuParam param) {
        Menu menu = menuConverter.convertParamToModel(param);
        menuMapper.insert(menu);
        //类别绑定状态
        if (MenuTypeEnum.IN.getType().equals(param.getType())) {
            categoryRepository.bindCategory(param.getCategoryId(), BindStatusEnum.BIND.getType());
        }
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu updateMenuInfo(MenuParam param) throws Exception {
        Menu menu = menuMapper.selectByPrimaryKey(param.getId());
        if (menu == null) {
            throw new BusinessException(ResultEnum.MENU_NOT_EXISTS);
        }
        //取消原来绑定的类别，新增别的绑定类别
        if (menu.getCategoryId() != 0 && !menu.getCategoryId().equals(param.getCategoryId())) {
            categoryRepository.bindCategory(menu.getCategoryId(), BindStatusEnum.UNBIND.getType());
            if (param.getCategoryId() != 0) {
                categoryRepository.bindCategory(param.getCategoryId(), BindStatusEnum.BIND.getType());
            }
        }
        menu = menuConverter.convertParamToModel(param);
        menuMapper.updateByPrimaryKeySelective(menu);
        return menu;
    }

    @Override
    public PageResponse<MenuDTO> queryMenuList(MenuQuery query) {
        PageMethod.startPage(query.getPageNo(), query.getPageSize());
        List<Menu> menuList = menuMapper.queryPageListByParam(query);
        PageInfo<Menu> page = new PageInfo<>(menuList);
        List<MenuDTO> contentDtoList = menuConverter.convertModelListToDtoList(page.getList());
        for (MenuDTO menuDTO : contentDtoList) {
            if (menuDTO.getCategoryId() != null) {
                CategoryDTO categoryDTO = categoryRepository.queryCategoryById(menuDTO.getCategoryId());
                menuDTO.setCategoryName(categoryDTO == null ? "" : categoryDTO.getName());
            }
        }
        return new PageResponse<>(query, page.getTotal(), contentDtoList);
    }

    @Override
    public MenuDTO queryMenuById(Integer menuId) {
        Menu menu = menuMapper.selectByPrimaryKey(menuId);
        return menuConverter.convertModelToDto(menu);
    }

    @Override
    public void updateMenuUrl(Integer categoryId, String newUrl) {
        ContentCategory contentCategory = categoryMapper.selectByPrimaryKey(categoryId);
        if (contentCategory != null && BindStatusEnum.BIND.getType().equals(contentCategory.getBindStatus())) {
            Menu menu = menuMapper.selectByUrl(contentCategory.getCode());
            if (menu != null) {
                menu = menuConverter.convertToModel(menu, newUrl);
                menuMapper.updateByPrimaryKeySelective(menu);
            }
        }

    }
}
