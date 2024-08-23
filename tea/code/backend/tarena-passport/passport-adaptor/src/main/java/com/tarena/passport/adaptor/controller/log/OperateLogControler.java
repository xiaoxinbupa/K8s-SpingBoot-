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

package com.tarena.passport.adaptor.controller.log;

import com.tarena.passport.doman.service.IOperateLogService;
import com.tarena.passport.protocol.pojo.model.OperateDetailDO;
import com.tarena.passport.protocol.pojo.query.UserOperateQuery;
import com.tarena.passport.protocol.result.JsonResult;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/log/operate")
@RestController
public class OperateLogControler {

    @Autowired
    private IOperateLogService operateLogService;

    @PostMapping("/list")
    public JsonResult<List<OperateView>> getList(HttpServletRequest request,
        @RequestBody UserOperateQuery userOperateQuery) {
        List<OperateDetailDO> list = operateLogService.getList();
        List<OperateView> operateViews = new ArrayList<>();
        for (OperateDetailDO operateDetail : list) {
            OperateView operateView = new OperateView();
            BeanUtils.copyProperties(operateDetail, operateView);
            operateViews.add(operateView);
        }
        return JsonResult.ok(operateViews);

    }

    @PostMapping("/delete/{id}")
    public JsonResult getAll(@PathVariable Long id) {
        operateLogService.deleteById(id);
        return JsonResult.ok("删除成功");
    }

}
