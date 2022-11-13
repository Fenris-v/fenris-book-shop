package com.example.FenrisBookShopApp.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class WebMvcConfig implements WebMvcConfigurer {

    private final SearchAddingHandlerInterceptor searchAddingHandlerInterceptor;

    public WebMvcConfig(SearchAddingHandlerInterceptor searchAddingHandlerInterceptor) {
        this.searchAddingHandlerInterceptor = searchAddingHandlerInterceptor;
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(searchAddingHandlerInterceptor);
    }
}
