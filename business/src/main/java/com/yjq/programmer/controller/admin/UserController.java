package com.yjq.programmer.controller.admin;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-11-18 15:46
 */
@RestController("AdminUserController")
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 后台保存用户数据(添加、修改)
     * @param userDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveUser(@RequestBody UserDTO userDTO){
        return userService.saveUser(userDTO);
    }

    /**
     * 后台分页获取用户数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<UserDTO>> getUserListByPage(@RequestBody PageDTO<UserDTO> pageDTO){
        return userService.getUserListByPage(pageDTO);
    }

    /**
     * 后台删除用户数据
     * @param userDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeUser(@RequestBody UserDTO userDTO){
        return userService.removeUser(userDTO);
    }

    /**
     * 后台获取用户总数
     * @return
     */
    @PostMapping("/total")
    public ResponseDTO<Long> getUserTotal(){
        return userService.getUserTotal();
    }

    /**
     * 后台获取在线用户
     * @return
     */
    @PostMapping("/online")
    public ResponseDTO<List<UserDTO>> getOnlineUser(){
        return userService.getOnlineUser();
    }

    /**
     * 后台用户登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseDTO<UserDTO> login(@RequestBody UserDTO userDTO){
        return userService.adminLogin(userDTO);
    }

    /**
     * 后台检查管理员是否登录
     * @param userDTO
     * @return
     */
    @PostMapping("/check_login")
    public ResponseDTO<UserDTO> checkLogin(@RequestBody UserDTO userDTO){
        return userService.checkLogin(userDTO);
    }

    /**
     * 后台用户退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResponseDTO<Boolean> logout(@RequestBody UserDTO userDTO){
        return userService.logout(userDTO);
    }

}
