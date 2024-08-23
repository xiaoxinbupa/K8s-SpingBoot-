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

import com.tarena.tp.tea.admin.dto.ContentDTO;
import com.tarena.tp.tea.admin.param.AuditContentParam;
import com.tarena.tp.tea.admin.param.ContentParam;
import com.tarena.tp.tea.admin.param.EnableStatusParam;
import com.tarena.tp.tea.admin.query.ContentQuery;
import com.tedu.inn.protocol.model.PageResponse;

public interface ContentRepository {

    void saveContentInfo(ContentParam contentParam) throws Exception;

    PageResponse<ContentDTO> getContentListByParam(ContentQuery contentQuery);

    void auditContent(AuditContentParam auditContentParam) throws Exception;

    void updateContentStatus(EnableStatusParam enableStatusParam);

    ContentDTO getContentInfoById(Integer contentId);

}
