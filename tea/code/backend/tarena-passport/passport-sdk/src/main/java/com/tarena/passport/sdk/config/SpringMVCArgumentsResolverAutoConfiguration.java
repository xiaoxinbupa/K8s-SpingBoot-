package com.tarena.passport.sdk.config;

import com.tarena.passport.sdk.LoginUserArgumentResolvers;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(LoginUserArgumentResolvers.class)
public class SpringMVCArgumentsResolverAutoConfiguration implements WebMvcConfigurer {
    @Resource
    private LoginUserArgumentResolvers loginUserArgumentResolvers;
    @Override public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolvers);
    }
}
