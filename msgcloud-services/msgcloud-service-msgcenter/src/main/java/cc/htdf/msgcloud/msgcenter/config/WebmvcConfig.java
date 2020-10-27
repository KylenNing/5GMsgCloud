package cc.htdf.msgcloud.msgcenter.config;

import cc.htdf.msgcloud.msgcenter.handler.LoginUserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: JT
 * date: 2020/8/19
 * title:
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserHandlerMethodArgumentResolver);
    }
}
