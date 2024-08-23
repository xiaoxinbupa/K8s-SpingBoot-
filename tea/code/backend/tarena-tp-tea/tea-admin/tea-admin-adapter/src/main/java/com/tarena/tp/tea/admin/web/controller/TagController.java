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

import com.tarena.tp.tea.admin.dto.TagDTO;
import com.tarena.tp.tea.admin.manager.TagService;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.param.TagParam;
import com.tarena.tp.tea.admin.query.TagQuery;
import com.tarena.tp.tea.admin.web.assemble.TagAssemble;
import com.tarena.tp.tea.admin.web.vo.TagVO;
import com.tedu.inn.protocol.model.PageResponse;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "tag", tags = "标签管理")
@Slf4j
@Validated
@RestController
@RequestMapping("tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagAssemble tagAssemble;

    @PostMapping("save")
    @ApiOperation(value = "保存标签/保存标签类型")
    public Result<Void> saveTagInfo(@Valid @RequestBody TagParam param) throws Exception {
        tagService.saveTagInfo(param);
        return Result.success();
    }

    @PostMapping("list")
    @ApiOperation(value = "查看标签列表/查看标签类型列表")
    public Result<PageResponse<TagVO>> getTagListByParam(@Validated @RequestBody TagQuery tagQuery) throws Exception {
        PageResponse<TagDTO> page = tagService.getTagListByParam(tagQuery);
        List<TagVO> vos = tagAssemble.assembleDtosToVos(page.getObjects());
        return new Result<>(new PageResponse<>(tagQuery, page.getTotal(), vos));
    }

    @GetMapping("type/list")
    @ApiOperation(value = "查看标签类型列表[不分页]")
    public Result<List<TagVO>> getTagListByParam() {
        List<TagDTO> tagDtoList = tagService.getTagTypeList();
        return new Result<>(tagAssemble.assembleDtosToVos(tagDtoList));
    }

    @GetMapping("type/tree")
    @ApiOperation(value = "查看标签树")
    public Result<Map<String,List<TagDTO>>> getTagTree() {
        return new Result<>(tagService.getTagTree());
    }

    @PostMapping("enable")
    @ApiOperation(value = "标签的启用")
    public Result<Void> enableTagInfo(@Validated @RequestBody EnableStatusParam enableStatusParam) throws Exception {
        tagService.enableTagInfo(enableStatusParam);
        return Result.success();
    }

}
