package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.RoleDTO;

import java.util.List;


public interface IRoleService {

    // 分页获取角色数据
    ResponseDTO<PageDTO<RoleDTO>> getRoleListByPage(PageDTO<RoleDTO> pageDTO);

    // 保存角色数据(添加、修改)
    ResponseDTO<Boolean> saveRole(RoleDTO roleDTO);

    // 删除角色数据
    ResponseDTO<Boolean> removeRole(RoleDTO roleDTO);

    // 获取所有角色数据
    ResponseDTO<List<RoleDTO>> getAllRoleList();
}
