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
package com.tarena.tp.tea.server.web.controller;

import com.tarena.tp.tea.server.dto.TagDTO;
import com.tarena.tp.tea.server.manager.TagService;
import com.tarena.tp.tea.server.web.assemble.TagAssemble;
import com.tarena.tp.tea.server.web.vo.TagVO;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("list")
    @ApiOperation(value = "查看类型标签")
    public Result<List<TagVO>> getTagInfoByTagType(@RequestParam("tagType") Integer tagType) throws Exception {
        List<TagDTO> tagList = tagService.getTagInfoByTagType(tagType);
        return new Result<>(tagAssemble.assembleDtosToVos(tagList));
    }

}
