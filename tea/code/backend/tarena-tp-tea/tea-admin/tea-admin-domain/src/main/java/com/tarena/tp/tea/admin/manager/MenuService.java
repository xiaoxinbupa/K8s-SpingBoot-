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

import com.tarena.tp.attach.common.enums.BusinessTypeEnum;
import com.tarena.tp.attach.server.client.AttachApi;
import com.tarena.tp.attach.server.dto.AttachDTO;
import com.tarena.tp.attach.server.param.AttachUpdateParam;
import com.tarena.tp.attach.server.query.AttachQuery;
import com.tarena.tp.tea.admin.dto.CategoryDTO;
import com.tarena.tp.tea.admin.dto.MenuDTO;
import com.tarena.tp.tea.admin.param.MenuParam;
import com.tarena.tp.tea.admin.query.MenuQuery;
import com.tarena.tp.tea.admin.repository.CategoryRepository;
import com.tarena.tp.tea.admin.repository.MenuRepository;
import com.tarena.tp.tea.common.enums.MenuTypeEnum;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.po.Menu;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MenuService {

    @Resource
    private MenuRepository menuRepository;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private AttachApi attachApi;

    /**
     * 功能描述: 菜单添加
     */
    public void saveMenuInfo(MenuParam param) throws Exception {
        if (MenuTypeEnum.IN.getType().equals(param.getType())) {
            CategoryDTO category = categoryRepository.queryCategoryById(param.getCategoryId());
            if (category == null || category.getParentId() != 0) {
                throw new BusinessException(ResultEnum.CATEGORY_ERROR);
            }
            param.setUrl(category.getCode());
        }
        Menu menu = new Menu();
        if (param.getId() == null) {
            menu = menuRepository.insertMenuInfo(param);
        } else {
            menu = menuRepository.updateMenuInfo(param);
        }
        if (param.getFileId() != null && !StringUtils.isEmpty(param.getIcon())) {
            updateAttachFileInfo(param.getFileId(), menu.getId());
        }
    }

    /**
     * 功能描述: 菜单列表
     */
    public PageResponse<MenuDTO> queryMenuList(MenuQuery query) {
        return menuRepository.queryMenuList(query);
    }

    /**
     * 功能描述: 菜单详情
     */
    public MenuDTO queryMenuById(Integer menuId) {
        MenuDTO menuDTO = menuRepository.queryMenuById(menuId);
        menuDTO.setFileId(getAttachFileIdByBusinessId(menuId));
        if (menuDTO.getCategoryId() != null) {
            CategoryDTO categoryDTO = categoryRepository.queryCategoryById(menuDTO.getCategoryId());
            menuDTO.setCategoryName(categoryDTO == null ? "" : categoryDTO.getName());
        }
        return menuDTO;
    }

    /**
     * 功能描述: 批量更新文件封面
     */
    public void updateAttachFileInfo(Integer fileId, Integer menuId) throws Exception {
        AttachUpdateParam attachUpdateParam = new AttachUpdateParam();
        attachUpdateParam.setBusinessId(menuId);
        attachUpdateParam.setBusinessType(BusinessTypeEnum.TEA.getType());
        attachUpdateParam.setId(fileId);
        attachUpdateParam.setIsCover(0);
        attachApi.batchUpdateAttachByIdList(Lists.newArrayList(attachUpdateParam));
    }

    /**
     * 功能描述: 获取文件fileId
     */
    public Integer getAttachFileIdByBusinessId(Integer menuId) {
        Integer fileId = null;
        AttachQuery attachQuery = new AttachQuery();
        attachQuery.setBusinessId(menuId);
        attachQuery.setBusinessType(BusinessTypeEnum.TEA.getType());
        List<AttachDTO> attachDTOList = attachApi.getAttachInfoByParam(attachQuery);
        if (!CollectionUtils.isEmpty(attachDTOList)) {
            fileId = attachDTOList.get(0).getId();
        }
        return fileId;
    }

}
