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
package com.tarena.tp.tea.admin.repository;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.tarena.passport.protocol.LoginUser;
import com.tarena.passport.sdk.context.SecurityContext;
import com.tarena.tp.tea.admin.converter.ContentConverter;
import com.tarena.tp.tea.admin.converter.ContentDetailConverter;
import com.tarena.tp.tea.admin.dao.ContentCategoryMapper;
import com.tarena.tp.tea.admin.dao.ContentDetailMapper;
import com.tarena.tp.tea.admin.dao.ContentMapper;
import com.tarena.tp.tea.admin.dto.ContentDTO;
import com.tarena.tp.tea.admin.param.AuditContentParam;
import com.tarena.tp.tea.admin.param.ContentParam;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.query.ContentQuery;
import com.tarena.tp.tea.po.Content;
import com.tarena.tp.tea.po.ContentCategory;
import com.tarena.tp.tea.po.ContentDetail;
import com.tedu.inn.protocol.model.PageResponse;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ContentRepositoryImpl implements ContentRepository {

    @Resource
    private ContentConverter contentConverter;

    @Resource
    private ContentDetailConverter contentDetailConverter;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private ContentCategoryMapper contentCategoryMapper;

    @Resource
    private TagRepository tagRepository;

    @Resource
    private ContentDetailMapper contentDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveContentInfo(ContentParam contentParam) throws Exception {
        //1.标签关键字
        String keywords = tagRepository.getTagKeywordsByIdList(contentParam.getTagIds());
        contentParam.setKeywords(keywords);
        //2.内容表插入
        ContentDetail contentDetail = contentDetailConverter.convertParamToModel(contentParam);
        Content content = contentConverter.convertParamToModel(contentParam);
        content.setCoverUrl(StringUtils.isEmpty(contentParam.getCoverUrl()) ? "" : contentParam.getCoverUrl());
        if (content.getId() == null) {
            contentMapper.insert(content);
            //2.详情表插入
            contentDetail.setContentId(content.getId());
            contentDetailMapper.insert(contentDetail);
        } else {
            contentMapper.updateByPrimaryKeySelective(content);
            //2.详情表更新
            contentDetail.setContentId(content.getId());
            contentDetailMapper.updateByContentId(contentDetail);
        }
    }

    @Override
    public PageResponse<ContentDTO> getContentListByParam(ContentQuery query) {
        PageMethod.startPage(query.getPageNo(), query.getPageSize());
        List<Content> contentList = contentMapper.getContentListByParam(query);
        PageInfo<Content> page = new PageInfo<>(contentList);
        List<ContentDTO> contentDtoList = contentConverter.convertModelListToDtoList(page.getList());
        if (!CollectionUtils.isEmpty(contentDtoList)) {
            for (ContentDTO contentDTO : contentDtoList) {
                ContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(contentDTO.getCategoryId());
                contentDTO.setCategoryName(contentCategory == null ? "" : contentCategory.getName());
            }
        }
        return new PageResponse<>(query, page.getTotal(), contentDtoList);
    }

    @Override
    public void auditContent(AuditContentParam auditContentParam) throws Exception {
        LoginUser loginUser = SecurityContext.getLoginToken();
        contentMapper.auditContent(auditContentParam.getContentId(), auditContentParam.getAuditStatus(), auditContentParam.getRemark(), System.currentTimeMillis(), loginUser.getUserId(), loginUser.getUserName());
    }

    @Override
    public void updateContentStatus(EnableStatusParam enableStatusParam) {
        contentMapper.updateStatusByIdList(enableStatusParam.getStatus(), enableStatusParam.getIds());
    }

    @Override
    public ContentDTO getContentInfoById(Integer contentId) {
        Content content = contentMapper.selectByPrimaryKey(contentId);
        return contentConverter.convertModelToDto(content);
    }
}
