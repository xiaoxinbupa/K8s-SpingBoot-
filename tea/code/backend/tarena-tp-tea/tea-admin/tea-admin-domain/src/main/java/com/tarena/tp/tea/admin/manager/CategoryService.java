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

import com.tarena.tp.tea.admin.dto.CategoryDTO;
import com.tarena.tp.tea.admin.param.CategoryParam;
import com.tarena.tp.tea.admin.query.CategoryQuery;
import com.tarena.tp.tea.admin.repository.CategoryRepository;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    /**
     * 功能描述: 菜单保存
     */
    public void saveCategoryInfo(CategoryParam param) throws Exception {
        categoryRepository.saveCategoryInfo(param);
    }

    /**
     * 功能描述: 菜单列表
     */
    public PageResponse<CategoryDTO> queryCategoryList(CategoryQuery query) {
        return categoryRepository.queryCategoryList(query);
    }

    /**
     * 功能描述: 未绑定菜单的一级类别列表
     */
    public List<CategoryDTO> getCategoryListByStatus(Integer status) throws Exception {
        return categoryRepository.getCategoryListByStatus(status);
    }

    /**
     * 功能描述: 类目树
     */
    public List<CategoryDTO> queryCategoryTree() {
        return categoryRepository.queryCategoryTree();
    }

    /**
     * 功能描述: 菜单详情
     */
    public CategoryDTO queryCategoryById(Integer categoryId) {
        return categoryRepository.queryCategoryById(categoryId);
    }

}
