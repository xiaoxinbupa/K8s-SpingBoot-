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
package com.tarena.tp.tea.server.data.converter;

import com.tarena.tp.tea.po.Menu;
import com.tarena.tp.tea.server.dto.MenuDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class MenuConverter {

    public MenuDTO convertModelToDto(Menu source) {
        if (source == null) {
            return null;
        }
        MenuDTO target = new MenuDTO();
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public List<MenuDTO> convertModelListToDtoList(List<Menu> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<MenuDTO> targets = new ArrayList<>();
        for (Menu source : sources) {
            MenuDTO target = new MenuDTO();
            BeanUtils.copyProperties(source,target);
            targets.add(target);
        }
        return targets;
    }
}
