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

package com.tarena.passport.doman.service;


import com.tarena.passport.protocol.pojo.model.UserDO;
import com.tarena.passport.protocol.pojo.param.UserAddressAndBrowserNameParam;
import com.tarena.passport.protocol.pojo.param.UserLoginParam;
import com.tarena.passport.protocol.pojo.param.UserParam;
import com.tarena.passport.protocol.pojo.query.UserQuery;
import com.tarena.passport.protocol.result.JsonPage;
import com.tarena.passport.protocol.result.TokenResult;
import com.tedu.inn.protocol.exception.BusinessException;
import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    void addNewUser(UserParam userParam, HttpServletRequest request) throws BusinessException;

    TokenResult login(UserLoginParam userLoginParam, UserAddressAndBrowserNameParam param,
        HttpServletRequest request) throws BusinessException;

    UserDO getUserDetails(HttpServletRequest request) throws BusinessException;

    JsonPage<UserDO> getUserList(UserQuery query, HttpServletRequest request, Integer page,
        Integer pageSize) throws BusinessException;

    void deleteUserById(Long id, HttpServletRequest request) throws BusinessException;

    UserDO selectUserById(Long id, HttpServletRequest request);

    void updateUser(UserParam user, HttpServletRequest request) throws BusinessException;

    void setEnable(Long id, HttpServletRequest request) throws BusinessException;

    void setDisable(Long id, HttpServletRequest request) throws BusinessException;
}
