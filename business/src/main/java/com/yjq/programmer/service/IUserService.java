package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;

import java.util.List;


public interface IUserService {

    // 用户注册操作
    ResponseDTO<Boolean> register(UserDTO userDTO);

    // 根据token获取用户信息
    ResponseDTO<UserDTO> getUserByToken(UserDTO userDTO);

    // 前台登录操作
    ResponseDTO<UserDTO> webLogin(UserDTO userDTO);

    // 分页获取用户数据
    ResponseDTO<PageDTO<UserDTO>> getUserListByPage(PageDTO<UserDTO> pageDTO);

    // 保存用户数据(添加、修改)
    ResponseDTO<Boolean> saveUser(UserDTO userDTO);

    // 删除用户数据
    ResponseDTO<Boolean> removeUser(UserDTO userDTO);

    // 后台登录操作
    ResponseDTO<UserDTO> adminLogin(UserDTO userDTO);

    // 检查用户是否登录
    ResponseDTO<UserDTO> checkLogin(UserDTO userDTO);

    // 退出登录操作
    ResponseDTO<Boolean> logout(UserDTO userDTO);

    // 修改个人信息操作
    ResponseDTO<Boolean> updateUserInfo(UserDTO userDTO);

    // 获取用户总数
    ResponseDTO<Long> getUserTotal();

    // 获取在线用户
    ResponseDTO<List<UserDTO>> getOnlineUser();
}
