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

import com.tarena.tp.tea.common.enums.ContentOperateTypeEnum;
import com.tarena.tp.tea.server.dto.ContentDTO;
import com.tarena.tp.tea.server.dto.ContentDetailDTO;
import com.tarena.tp.tea.server.manager.ContentService;
import com.tarena.tp.tea.server.query.CommentQuery;
import com.tarena.tp.tea.server.query.ContentQuery;
import com.tarena.tp.tea.server.query.HotContentQuery;
import com.tarena.tp.tea.server.query.MenuContentQuery;
import com.tarena.tp.tea.server.web.assemble.ContentAssemble;
import com.tarena.tp.tea.server.web.assemble.ContentDetailAssemble;
import com.tarena.tp.tea.server.web.vo.ContentDetailVO;
import com.tarena.tp.tea.server.web.vo.ContentVO;
import com.tedu.inn.protocol.model.PageResponse;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "content", tags = "内容管理")
@Slf4j
@Validated
@RestController
@RequestMapping("content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentAssemble contentAssemble;

    @Autowired
    private ContentDetailAssemble contentDetailAssemble;

    @PostMapping("menu/list")
    @ApiOperation(value = "查看菜单下的文章列表")
    public Result<PageResponse<ContentVO>> getMenuContentListByParam(@Validated @RequestBody MenuContentQuery menuContentQuery) throws Exception {
        PageResponse<ContentDTO> page = contentService.getMenuContentListByParam(menuContentQuery);
        List<ContentVO> vos = contentAssemble.assembleDtosToVos(page.getObjects());
        return new Result<>(new PageResponse<>(menuContentQuery, page.getTotal(), vos));
    }

    @PostMapping("list")
    @ApiOperation(value = "查看类别下的文章列表")
    public Result<PageResponse<ContentVO>> getContentListByParam(@Validated @RequestBody ContentQuery contentQuery) throws Exception {
        PageResponse<ContentDTO> page = contentService.getContentListByParam(contentQuery);
        List<ContentVO> vos = contentAssemble.assembleDtosToVos(page.getObjects());
        return new Result<>(new PageResponse<>(contentQuery, page.getTotal(), vos));
    }

    @PostMapping("hot")
    @ApiOperation(value = "查看热门文章列表")
    public Result<List<ContentVO>> getHotContentList(@Validated @RequestBody HotContentQuery hotContentQuery) throws Exception {
        List<ContentDTO> contentList = contentService.getHotContentList(hotContentQuery);
        return new Result<>(contentAssemble.assembleDtosToVos(contentList));
    }

    @PostMapping("up")
    @ApiOperation(value = "内容顶")
    public Result<Void> upContent(Integer contentId) throws Exception {
        contentService.operateContent(contentId, ContentOperateTypeEnum.UP.getType());
        return Result.success();
    }

    @PostMapping("down")
    @ApiOperation(value = "内容踩")
    public Result<Void> downContent(Integer contentId) throws Exception {
        contentService.operateContent(contentId,ContentOperateTypeEnum.DOWN.getType());
        return Result.success();
    }

    @PostMapping("click")
    @ApiOperation(value = "内容点击")
    public Result<Void> clickContent(Integer contentId) throws Exception {
        contentService.operateContent(contentId,ContentOperateTypeEnum.CLICK.getType());
        return Result.success();
    }


    @PostMapping("detail")
    @ApiOperation(value = "文章详情")
    public Result<ContentDetailVO> getContentInfoById(@Valid @RequestBody CommentQuery query) throws Exception {
        ContentDetailDTO contentDetailDTO = contentService.getContentInfoById(query);
        ContentDetailVO contentDetailVO  = contentDetailAssemble.assembleDtoToVo(contentDetailDTO);
        return new Result<>(contentDetailVO);
    }

}
