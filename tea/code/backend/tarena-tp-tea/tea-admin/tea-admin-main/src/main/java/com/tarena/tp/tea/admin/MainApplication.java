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
package com.tarena.tp.tea.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;


@SpringBootApplication
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class,
        basePackages = {
            "com.tarena.tp.tea",
            "com.tarena.stock", //库存
            "com.tarena.mall.order.admin",//订单
            "com.tarena.simple.goods",//商品
            "com.tarena.passport",
            "com.tarena.tp.attach"//附件
        },
        excludeFilters = {@ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = {
                        //com.tarena.mall.order.admin.dao.mybaits.plus.MybatisPlusConfiguration.class
                    })})
@MapperScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class,
        value = {
            "com.tarena.tp.tea.admin.dao",
            "com.tarena.simple.goods.admin.dao",
            "com.tarena.simple.goods.server.dao",
            "com.tarena.stock.admin.dao",
            "com.tarena.mall.order.admin.dao.mybaits.plus.api",
            "com.tarena.tp.attach.server.dao"
        })
@EnableAspectJAutoProxy(exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
