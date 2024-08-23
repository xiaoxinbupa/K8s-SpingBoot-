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
import com.tarena.tp.tea.admin.dto.ContentDTO;
import com.tarena.tp.tea.admin.dto.DetailDTO;
import com.tarena.tp.tea.admin.param.AuditContentParam;
import com.tarena.tp.tea.admin.param.ContentParam;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.query.ContentQuery;
import com.tarena.tp.tea.admin.repository.CategoryRepository;
import com.tarena.tp.tea.admin.repository.ContentDetailRepository;
import com.tarena.tp.tea.admin.repository.ContentRepository;
import com.tarena.tp.tea.common.enums.ResultEnum;
import com.tarena.tp.tea.common.util.NetworkUtil;
import com.tedu.inn.protocol.exception.BusinessException;
import com.tedu.inn.protocol.model.PageResponse;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContentService {

    @Value("${image_path}")
    private String imagePath;

    @Value("${url}")
    private String url;

    @Resource
    private ContentRepository contentRepository;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private ContentDetailRepository contentDetailRepository;

    /**
     * 功能描述: 发布内容
     */
    public void saveContentInfo(HttpServletRequest request, ContentParam contentParam) throws Exception {
        contentParam.setIp(NetworkUtil.getIpAddr(request));
        //0.类别数据校验
        CategoryDTO categoryDTO = categoryRepository.queryCategoryById(contentParam.getCategoryId());
        if (categoryDTO == null) {
            throw new BusinessException(ResultEnum.CATEGORY_NOT_EXISTS);
        }
        contentRepository.saveContentInfo(contentParam);
    }

    /**
     * 功能描述: 内容审核
     */
    public void auditContent(AuditContentParam auditContentParam) throws Exception {
        ContentDTO contentDTO = contentRepository.getContentInfoById(auditContentParam.getContentId());
        if (contentDTO == null) {
            throw new BusinessException(ResultEnum.CONTENT_NOT_EXISTS);
        }
        contentRepository.auditContent(auditContentParam);
    }

    /**
     * 功能描述: 查看文章列表
     */
    public PageResponse<ContentDTO> getContentListByParam(ContentQuery contentQuery) {
        return contentRepository.getContentListByParam(contentQuery);
    }

    /**
     * 功能描述: 内容上下架
     */
    public void enableContentInfo(EnableStatusParam enableStatusParam) throws Exception {
        contentRepository.updateContentStatus(enableStatusParam);
    }

    /**
     * 功能描述: 查看文章和评论详情
     */
    public ContentDTO getContentInfoById(Integer id) {
        ContentDTO contentDTO = contentRepository.getContentInfoById(id);
        DetailDTO detailDTO = contentDetailRepository.getDetailInfoByContentId(id);
        contentDTO.setDetail(detailDTO.getDetail());
        return contentDTO;
    }


}
