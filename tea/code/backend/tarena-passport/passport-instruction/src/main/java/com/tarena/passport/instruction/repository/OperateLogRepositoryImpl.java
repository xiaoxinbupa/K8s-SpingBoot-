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

package com.tarena.passport.instruction.repository;


import com.tarena.passport.doman.repository.OperateLogRepository;
import com.tarena.passport.instruction.mapper.UserLogMapper;
import com.tarena.passport.protocol.pojo.model.OperateDetailDO;
import com.tarena.passport.protocol.pojo.param.UserOperateParam;
import com.tarena.passport.protocol.pojo.query.UserOperateQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OperateLogRepositoryImpl implements OperateLogRepository {

    @Autowired
    private UserLogMapper userLogMapper;

    @Override public int insertOperateLog(UserOperateParam userOperateParam) {
        return userLogMapper.insertOperateLog(userOperateParam);
    }

    @Override public List<OperateDetailDO> getList(UserOperateQuery userOperateQuery) {
        return userLogMapper.getList(userOperateQuery);
    }

    @Override public int deleteOperateLogById(Long id) {
        return userLogMapper.deleteOperateLogById(id);
    }

    @Override public int deleteLoginLogById(Long id) {
        return userLogMapper.deleteLoginLogById(id);
    }
}
