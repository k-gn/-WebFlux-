package com.cos.fluxtest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
public class MyFilterConfig {

    private final EventNotify eventNotify;

    // 필터 등록하기
    @Bean
    public FilterRegistrationBean<Filter> addFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter(eventNotify));
        bean.addUrlPatterns("/sse");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> addFilter2() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter2(eventNotify));
        bean.addUrlPatterns("/add");
        return bean;
    }
}
