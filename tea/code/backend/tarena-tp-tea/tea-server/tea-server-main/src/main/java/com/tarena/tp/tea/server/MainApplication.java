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
package com.tarena.tp.tea.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.tarena.tp.tea.server",
    "com.tarena.mall.user.server", //用户
    "com.tarena.simple.goods",//商品
    "com.tarena.shopping.cart",//购物车
    "com.tarena.price",//价格
    "com.tarena.stock",//库存
    "com.tarena.mall.order",//订单
    "com.tarena.tp.attach.server", //附件
    "com.tarena.tp.basic.server" //基础数据
},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                    com.tarena.mall.order.server.dao.mybaits.plus.MybatisPlusConfiguration.class
                })})
@MapperScan({
    "com.tarena.tp.tea.server.dao",
    "com.tarena.mall.user.server.dao",
    "com.tarena.simple.goods.server.dao",
    "com.tarena.stock.server.dao",
    "com.tarena.mall.order.server.dao.mybaits.plus.api",
    "com.tarena.tp.attach.server.dao",
    "com.tarena.tp.basic.server.dao"
})
@EnableAspectJAutoProxy(exposeProxy = true)
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
