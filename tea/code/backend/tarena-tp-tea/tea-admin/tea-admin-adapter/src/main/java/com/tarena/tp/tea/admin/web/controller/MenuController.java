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

import com.tarena.tp.tea.admin.dto.MenuDTO;
import com.tarena.tp.tea.admin.manager.MenuService;
import com.tarena.tp.tea.admin.param.MenuParam;
import com.tarena.tp.tea.admin.query.MenuQuery;
import com.tarena.tp.tea.admin.web.assemble.MenuAssemble;
import com.tarena.tp.tea.admin.web.vo.MenuVO;
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

@Api(value = "menu", tags = "菜单管理")
@Slf4j
@Validated
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuAssemble menuAssemble;

    @PostMapping("save")
    @ApiOperation(value = "添加修改菜单")
    public Result<Void> saveMenuInfo(@Valid @RequestBody MenuParam param) throws Exception {
        menuService.saveMenuInfo(param);
        return Result.success();
    }

    @GetMapping("detail")
    @ApiOperation(value = "菜单详情")
    public Result<MenuVO> detail(Integer menuId) {
        MenuDTO menuDTO = menuService.queryMenuById(menuId);
        return new Result<>(menuAssemble.assembleDtoToVo(menuDTO));
    }

    @PostMapping("list")
    @ApiOperation(value = "菜单列表")
    public Result<PageResponse<MenuVO>> queryMenuList(@Valid @RequestBody MenuQuery query) throws Exception {
        PageResponse<MenuDTO> page = menuService.queryMenuList(query);
        List<MenuVO> vos = menuAssemble.assembleDtosToVos(page.getObjects());
        return new Result<>(new PageResponse<>(query, page.getTotal(), vos));
    }

}
