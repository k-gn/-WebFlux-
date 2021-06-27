package com.cos.fluxtest;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class MyFilterConfig {

    // 필터 등록하기
    @Bean
    public FilterRegistrationBean<Filter> addFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}
