package com.yjq.programmer.config;
/**
 * 用来配置拦截器的配置类
 */

import com.yjq.programmer.constant.WhiteList;
import com.yjq.programmer.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private UserLoginInterceptor userLoginInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//搜寻/**所有链接除了RuntimeConstant.loginExcludePathPatterns中的链接
		registry.addInterceptor(userLoginInterceptor).addPathPatterns("/**").excludePathPatterns(WhiteList.LoginExcludePathPatterns);;
	}

}
