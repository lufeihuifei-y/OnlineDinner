package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController("WebUserController")
@RequestMapping("/web/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 用户注册操作处理
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseDTO<Boolean> register(@RequestBody UserDTO userDTO){
        return userService.register(userDTO);
    }

    /**
     * 前台登录操作处理
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseDTO<UserDTO> webLogin(@RequestBody UserDTO userDTO){
        return userService.webLogin(userDTO);
    }

    /**
     * 前台用户退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResponseDTO<Boolean> logout(@RequestBody UserDTO userDTO){
        return userService.logout(userDTO);
    }

    /**
     * 前台检查用户是否登录
     * @param userDTO
     * @return
     */
    @PostMapping("/check_login")
    public ResponseDTO<UserDTO> checkLogin(@RequestBody UserDTO userDTO){
        return userService.checkLogin(userDTO);
    }


    /**
     * 通过token获取用户信息
     * @param userDTO
     * @return
     */
    @PostMapping("/token")
    public ResponseDTO<UserDTO> getUserByToken(@RequestBody UserDTO userDTO){
        return userService.getUserByToken(userDTO);
    }

    /**
     * 前台用户修改个人信息
     * @param userDTO
     * @return
     */
    @PostMapping("/update")
    public ResponseDTO<Boolean> updateUserInfo(@RequestBody UserDTO userDTO){
        return userService.updateUserInfo(userDTO);
    }
}
