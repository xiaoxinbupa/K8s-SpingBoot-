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

import com.tarena.tp.tea.admin.dto.ContentDTO;
import com.tarena.tp.tea.admin.manager.ContentService;
import com.tarena.tp.tea.admin.param.AuditContentParam;
import com.tarena.tp.tea.admin.param.ContentParam;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.query.ContentQuery;
import com.tarena.tp.tea.admin.web.assemble.ContentAssemble;
import com.tarena.tp.tea.admin.web.vo.ContentVO;
import com.tedu.inn.protocol.model.PageResponse;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "content", tags = "管理内容")
@Slf4j
@Validated
@RestController
@RequestMapping("content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentAssemble contentAssemble;

    @PostMapping("save")
    @ApiOperation(value = "发布内容")
    public Result<Void> saveContentInfo(HttpServletRequest request,@Valid @RequestBody ContentParam param) throws Exception {
        contentService.saveContentInfo(request,param);
        return Result.success();
    }

    @PostMapping("list")
    @ApiOperation(value = "查看文章列表")
    public Result<PageResponse<ContentVO>> getContentListByParam(@Validated @RequestBody ContentQuery contentQuery) throws Exception {
        PageResponse<ContentDTO> page = contentService.getContentListByParam(contentQuery);
        List<ContentVO> vos = contentAssemble.assembleDtosToVos(page.getObjects());
        return new Result<>(new PageResponse<>(contentQuery, page.getTotal(), vos));
    }

    @PostMapping("content/audit")
    @ApiOperation(value = "内容审核[auditStatus 0:待审核;1:审核通过;2:审核拒绝]")
    public Result<Void> auditContent(@Validated @RequestBody AuditContentParam auditContentParam) throws Exception {
        contentService.auditContent(auditContentParam);
        return Result.success();
    }

    @PostMapping("enable")
    @ApiOperation(value = "内容的批量上下架")
    public Result<Void> enableContentInfo(@Validated @RequestBody EnableStatusParam enableStatusParam) throws Exception {
        contentService.enableContentInfo(enableStatusParam);
        return Result.success();
    }

    @GetMapping("detail")
    @ApiOperation(value = "文章详情")
    public Result<ContentVO> getContentInfoById(Integer id) throws Exception {
        ContentDTO contentDTO = contentService.getContentInfoById(id);
        ContentVO contentVO  = contentAssemble.assembleDtoToVo(contentDTO);
        return new Result<>(contentVO);
    }

}
