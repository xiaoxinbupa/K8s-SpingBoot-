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
package com.tarena.passport.provider.config;


import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class KaptchaConfiguration {

    @Bean
    public Producer kaptchaProducer() {
        Properties properties = new Properties();
        //框的长度
        properties.setProperty("kaptcha.image.width", "100");
        //框的高度
        properties.setProperty("kaptcha.image.height", "40");
        //字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        //生成验证码的样本集合
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789QWERTYUIOPASDFGHJKLZXCVBNM");
        //生成的随机字符数
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //干扰类，噪声、阴影  默认防破解
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
