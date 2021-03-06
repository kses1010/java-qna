package com.codessquad.qna.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

        registry.addViewController("/qna/form").setViewName("qna/form");
        registry.addViewController("/qna/show").setViewName("qna/show");

        registry.addViewController("/user/login").setViewName("user/login");
        registry.addViewController("/user/form").setViewName("user/form");
        registry.addViewController("/user/profile").setViewName("user/profile");
    }
}
