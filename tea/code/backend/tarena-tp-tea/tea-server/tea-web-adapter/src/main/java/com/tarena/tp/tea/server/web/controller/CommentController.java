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
import com.tarena.tp.tea.server.manager.CommentService;
import com.tarena.tp.tea.server.param.CommentParam;
import com.tedu.inn.protocol.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "comment", tags = "评论管理")
@Slf4j
@Validated
@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("add")
    @ApiOperation(value = "用户发表评论")
    public Result<Void> userComment(HttpServletRequest request,@Valid @RequestBody CommentParam commentParam) throws Exception {
        commentService.userComment(request,commentParam);
        return Result.success();
    }

    @PostMapping("up")
    @ApiOperation(value = "评论顶")
    public Result<Void> upComment(Integer commentId) throws Exception {
        commentService.operateComment(commentId, ContentOperateTypeEnum.UP.getType());
        return Result.success();
    }

    @PostMapping("down")
    @ApiOperation(value = "评论踩")
    public Result<Void> downComment(Integer commentId) throws Exception {
        commentService.operateComment(commentId,ContentOperateTypeEnum.DOWN.getType());
        return Result.success();
    }

}
