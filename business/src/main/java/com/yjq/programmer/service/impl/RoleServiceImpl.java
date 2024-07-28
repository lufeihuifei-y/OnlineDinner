package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.RoleMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.Role;
import com.yjq.programmer.domain.RoleExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.domain.UserExample;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.RoleDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IRoleService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-01-01 11:54
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    /**
     * 分页获取角色数据
     * @param pageDTO
     * @return
     */
    public ResponseDTO<PageDTO<RoleDTO>> getRoleListByPage(PageDTO<RoleDTO> pageDTO){
        RoleExample roleExample = new RoleExample();
        // 判断是否进行关键字搜索
        if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
            roleExample.createCriteria().andNameLike("%"+pageDTO.getSearchContent()+"%");
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出角色数据
        List<Role> roleList = roleMapper.selectByExample(roleExample);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<RoleDTO> roleDTOList = CopyUtil.copyList(roleList, RoleDTO.class);
        pageDTO.setList(roleDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存角色数据(添加、修改)
     * @param roleDTO
     * @return
     */
    public ResponseDTO<Boolean> saveRole(RoleDTO roleDTO){
        if(roleDTO == null){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(roleDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Role role = CopyUtil.copy(roleDTO, Role.class);
        if(CommonUtil.isEmpty(role.getId())){
            // id为空 说明是添加数据
            // 判断角色名称是否存在
            if(isRoleNameExist(role, "")){
                return ResponseDTO.errorByMsg(CodeMsg.ROLE_NAME_EXIST);
            }
            // 生产8位id
            role.setId(UuidUtil.getShortUuid());
            // 添加数据到数据库
            if(roleMapper.insertSelective(role) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.ROLE_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            // 判断角色名称是否存在
            if(isRoleNameExist(role, role.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.ROLE_NAME_EXIST);
            }
            // 修改数据库中数据
            if(roleMapper.updateByPrimaryKeySelective(role) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.ROLE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除角色数据
     * @param roleDTO
     * @return
     */
    public ResponseDTO<Boolean> removeRole(RoleDTO roleDTO){
        if(roleDTO == null || CommonUtil.isEmpty(roleDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除属于该角色的所有信息
        UserExample userExample = new UserExample();
        userExample.createCriteria().andRoleIdEqualTo(roleDTO.getId());
        List<User> userList = userMapper.selectByExample(userExample);
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        for(UserDTO userDTO : userDTOList){
            userService.removeUser(userDTO);
        }
        // 删除角色信息
        if(roleMapper.deleteByPrimaryKey(roleDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.ROLE_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 判断角色名称是否重复
     * @param role
     * @param id
     * @return
     */
    public Boolean isRoleNameExist(Role role, String id) {
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria().andNameEqualTo(role.getName());
        List<Role> selectedRoleList = roleMapper.selectByExample(roleExample);
        if(selectedRoleList != null && selectedRoleList.size() > 0) {
            if(selectedRoleList.size() > 1){
                return true; //出现重名
            }
            if(!selectedRoleList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }

    /**
     * 获取所有角色数据
     * @return
     */
    public ResponseDTO<List<RoleDTO>> getAllRoleList(){
        List<Role> roleList = roleMapper.selectByExample(new RoleExample());
        List<RoleDTO> roleDTOList = CopyUtil.copyList(roleList, RoleDTO.class);
        return ResponseDTO.success(roleDTOList);
    }

}
