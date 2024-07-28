package com.yjq.programmer.controller.admin;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.RoleDTO;
import com.yjq.programmer.service.IRoleService;
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
 * @create 2021-11-17 13:46
 */
@RestController("AdminRoleController")
@RequestMapping("/admin/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 后台保存角色数据(添加、修改)
     * @param roleDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveRole(@RequestBody RoleDTO roleDTO){
        return roleService.saveRole(roleDTO);
    }

    /**
     * 后台分页获取角色数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<RoleDTO>> getRoleListByPage(@RequestBody PageDTO<RoleDTO> pageDTO){
        return roleService.getRoleListByPage(pageDTO);
    }

    /**
     * 后台获取所有角色数据
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<RoleDTO>> getAllRoleList(){
        return roleService.getAllRoleList();
    }

    /**
     * 后台删除角色数据
     * @param roleDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeRole(@RequestBody RoleDTO roleDTO){
        return roleService.removeRole(roleDTO);
    }
}
