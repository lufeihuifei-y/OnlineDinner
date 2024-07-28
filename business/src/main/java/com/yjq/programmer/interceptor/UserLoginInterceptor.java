package com.yjq.programmer.interceptor;

import com.alibaba.fastjson.JSON;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 前台用户登录拦截器
 * @author Administrator
 *
 */
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

	private Logger log = LoggerFactory.getLogger(UserLoginInterceptor.class);

	@Resource
	private IUserService userService;

	@Override
	public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String type = request.getHeader("type");
		response.setCharacterEncoding("UTF-8");
		if("axios".equals(type)){
			// 说明是axios请求
			String token = request.getHeader("token");
			if(CommonUtil.isEmpty(token)){
				try {
					response.getWriter().write(JSON.toJSONString(CodeMsg.USER_SESSION_EXPIRED));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}else {
				// 和Redis中数据进行校验
				UserDTO userDTO = new UserDTO();
				userDTO.setToken(token);
				ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
				if(responseDTO.getCode() != 0){
					try {
						response.getWriter().write(JSON.toJSONString(CodeMsg.USER_SESSION_EXPIRED));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
				return true;
			}
		}
		return true;
	}
}
