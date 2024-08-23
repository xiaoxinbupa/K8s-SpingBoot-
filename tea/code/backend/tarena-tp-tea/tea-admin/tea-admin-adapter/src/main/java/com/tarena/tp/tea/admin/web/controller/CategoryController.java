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
package com.tarena.tp.tea.admin.web.controller;

import com.tarena.tp.tea.admin.dto.CategoryDTO;
import com.tarena.tp.tea.admin.manager.CategoryService;
import com.tarena.tp.tea.admin.param.CategoryParam;
import com.tarena.tp.tea.admin.query.CategoryQuery;
import com.tarena.tp.tea.admin.web.assemble.CategoryAssemble;
import com.tarena.tp.tea.admin.web.vo.CategoryVO;
import com.tarena.tp.tea.common.enums.BindStatusEnum;
import com.tedu.inn.protocol.model.PageResponse;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "category", tags = "类别管理")
@Slf4j
@Validated
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryAssemble categoryAssemble;

    @PostMapping("save")
    @ApiOperation(value = "添加类别")
    public Result<Void> saveCategoryInfo(@Valid @RequestBody CategoryParam param) throws Exception {
        categoryService.saveCategoryInfo(param);
        return Result.success();
    }

    @PostMapping("list")
    @ApiOperation(value = "类别列表")
    public Result<PageResponse<CategoryVO>> queryCategoryList(
        @Valid @RequestBody CategoryQuery query) throws Exception {
        PageResponse<CategoryDTO> page = categoryService.queryCategoryList(query);
        List<CategoryVO> vos = categoryAssemble.assembleDtosToVos(page.getObjects());
        return new Result<>(new PageResponse<>(query, page.getTotal(), vos));
    }

    @PostMapping("list/bind")
    @ApiOperation(value = "未绑定的类别一级列表")
    public Result<List<CategoryVO>> queryBindMenuCategoryTree() throws Exception {
        List<CategoryDTO> categoryDtoList = categoryService.getCategoryListByStatus(BindStatusEnum.UNBIND.getType());
        return new Result<>(categoryAssemble.assembleDtosToVos(categoryDtoList));
    }

    @GetMapping("tree")
    @ApiOperation(value = "类别树")
    public Result<List<CategoryVO>> queryCategoryTree() throws Exception {
        List<CategoryDTO> categoryDtoList = categoryService.queryCategoryTree();
        List<CategoryVO> vos = categoryAssemble.assembleDtosToVos(categoryDtoList);
        return new Result<>(vos);
    }

    @GetMapping("detail")
    @ApiOperation(value = "类别详情")
    public Result<CategoryVO> detail(Integer categoryId) throws Exception {
        CategoryDTO categoryDTO = categoryService.queryCategoryById(categoryId);
        return new Result<>(categoryAssemble.assembleDtoToVo(categoryDTO));
    }

}
