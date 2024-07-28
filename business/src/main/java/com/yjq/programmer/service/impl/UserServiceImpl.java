package com.yjq.programmer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.CartMapper;
import com.yjq.programmer.dao.OrderMapper;
import com.yjq.programmer.dao.RoleMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IOrderService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-31 14:10
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private IOrderService orderService;

    @Resource
    private RoleMapper roleMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 用户注册操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> register(UserDTO userDTO) {
        if(userDTO == null){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 设置普通用户角色
        userDTO.setRoleId(RoleEnum.USER.getCode());
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        // 判断重复密码是否正确
        if(CommonUtil.isEmpty(userDTO.getRePassword())){
            return ResponseDTO.errorByMsg(CodeMsg.REPASSWORD_EMPTY);
        }
        if(!userDTO.getPassword().equals(userDTO.getRePassword())){
            return ResponseDTO.errorByMsg(CodeMsg.REPASSWORD_ERROR);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        // 判断用户昵称是否存在
        if(isUsernameExist(user, "")){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
        }
        // 设置主键id
        user.setId(UuidUtil.getShortUuid());
        // 角色为普通用户
        user.setRoleId(RoleEnum.USER.getCode());
        // 保存数据到数据库中
        if(userMapper.insertSelective(user) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.USER_REGISTER_ERROR);
        }
        return ResponseDTO.successByMsg(true, "注册成功，快登录体验吧！");
    }

    /**
     * 根据token获取用户信息
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> getUserByToken(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.success(new UserDTO());
        }
        ResponseDTO<UserDTO> loginUser = getLoginUser(userDTO.getToken());
        if(loginUser.getCode() != 0){
            return ResponseDTO.success(new UserDTO());
        }else {
            return loginUser;
        }
    }

    /**
     * 前台登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> webLogin(UserDTO userDTO) {
        // 进行是否为空判断
        if(CommonUtil.isEmpty(userDTO.getUsername())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }
        // 对比昵称和密码是否正确
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
        }
        // 生成登录token并存入Redis中
        UserDTO selectedUserDto = CopyUtil.copy(userList.get(0), UserDTO.class);
        String token = UuidUtil.getShortUuid();
        selectedUserDto.setToken(token);
        //把token存入redis中 有效期1小时
        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDto), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDto, "登录成功！");
    }

    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<UserDTO>> getUserListByPage(PageDTO<UserDTO> pageDTO) {
        UserExample userExample = new UserExample();
        // 判断是否进行关键字搜索
        if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
            userExample.createCriteria().andUsernameLike("%"+pageDTO.getSearchContent()+"%");
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出用户数据
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        // 分装角色信息
        for(UserDTO userDTO : userDTOList){
            Role role = roleMapper.selectByPrimaryKey(userDTO.getRoleId());
            RoleDTO roleDTO = CopyUtil.copy(role, RoleDTO.class);
            userDTO.setRoleDTO(roleDTO);
        }
        pageDTO.setList(userDTOList);
        return ResponseDTO.success(pageDTO);
    }


    @Override
    public ResponseDTO<Boolean> saveUser(UserDTO userDTO) {
        if(userDTO == null){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        if(CommonUtil.isEmpty(user.getId())){
            // id为空 说明是添加数据
            // 判断用户昵称是否存在
            if(isUsernameExist(user, "")){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            // 生产8位id
            user.setId(UuidUtil.getShortUuid());
            // 添加数据到数据库
            if(userMapper.insertSelective(user) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.USER_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            // 判断用户昵称是否存在
            if(isUsernameExist(user, user.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            // 修改数据库中数据
            if(userMapper.updateByPrimaryKeySelective(user) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    @Override
    public ResponseDTO<Boolean> removeUser(UserDTO userDTO) {
        if(userDTO == null || CommonUtil.isEmpty(userDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除与该用户有关的购物车信息
        CartExample cartExample = new CartExample();
        cartExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        cartMapper.deleteByExample(cartExample);
        // 删除与该用户有关的订单信息
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        List<OrderDTO> orderDTOList = CopyUtil.copyList(orderList, OrderDTO.class);
        for(OrderDTO orderDTO : orderDTOList){
            orderService.removeOrder(orderDTO);
        }
        // 删除用户信息
        if(userMapper.deleteByPrimaryKey(userDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.USER_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    @Override
    public ResponseDTO<UserDTO> adminLogin(UserDTO userDTO) {
        // 进行是否为空判断
        if(CommonUtil.isEmpty(userDTO.getUsername())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }
        // 对比昵称和密码是否正确
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
        }
        // 生成登录token并存入Redis中
        UserDTO selectedUserDto = CopyUtil.copy(userList.get(0), UserDTO.class);
        // 判断是不是管理员角色
        if(!selectedUserDto.getRoleId().equals(RoleEnum.ADMIN.getCode())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_IS_ADMIN);
        }
        String token = UuidUtil.getShortUuid();
        selectedUserDto.setToken(token);
        //把token存入redis中 有效期1小时
        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDto), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDto, "登录成功！");
    }

    /**
     * 检查用户是否登录
     * @param userDTO
     * @return
     */
    public ResponseDTO<UserDTO> checkLogin(UserDTO userDTO){
        if(userDTO == null || CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        ResponseDTO<UserDTO> responseDTO = getLoginUser(userDTO.getToken());
        if(responseDTO.getCode() != 0){
            return responseDTO;
        }
        logger.info("获取到的登录信息={}", responseDTO.getData());
        return ResponseDTO.success(responseDTO.getData());
    }

    /**
     *  退出登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> logout(UserDTO userDTO) {
        if(!CommonUtil.isEmpty(userDTO.getToken())){
            // token不为空  清除redis中数据
            stringRedisTemplate.delete("USER_" + userDTO.getToken());
        }
        return ResponseDTO.successByMsg(true, "退出登录成功！");
    }

    /**
     * 修改个人信息操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateUserInfo(UserDTO userDTO) {
        ResponseDTO<Boolean> responseDTO = saveUser(userDTO);
        if(responseDTO.getCode() != 0){
            return responseDTO;
        }
        // 说明更新成功了，那么我们就要更新Redis中信息
        stringRedisTemplate.opsForValue().set("USER_" + userDTO.getToken(), JSON.toJSONString(userDTO), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 获取用户总数
     * @return
     */
    @Override
    public ResponseDTO<Long> getUserTotal() {
        long total = userMapper.countByExample(new UserExample());
        return ResponseDTO.success(total);
    }

    /**
     * 获取在线用户
     * @return
     */
    @Override
    public ResponseDTO<List<UserDTO>> getOnlineUser() {
        List<UserDTO> userDTOList = new ArrayList<>();
        Set<String> keys = stringRedisTemplate.keys("USER_"  + "*");
        if(keys == null || keys.size() == 0){
            return ResponseDTO.success(userDTOList);
        }
        List<String> valueList = stringRedisTemplate.opsForValue().multiGet(keys);
        if(valueList == null || valueList.size() == 0){
            return ResponseDTO.success(userDTOList);
        }
        for(String value : valueList){
            if(!CommonUtil.isEmpty(value)){
                UserDTO userDTO = JSON.parseObject(value, UserDTO.class);
                userDTOList.add(userDTO);
            }
        }
        return ResponseDTO.success(userDTOList);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public ResponseDTO<UserDTO> getLoginUser(String token){
        String value = stringRedisTemplate.opsForValue().get("USER_" + token);
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        UserDTO selectedUserDTO = JSON.parseObject(value, UserDTO.class);
        return ResponseDTO.success(selectedUserDTO);
    }


    /**
     * 判断用户昵称是否重复
     * @param user
     * @param id
     * @return
     */
    public Boolean isUsernameExist(User user, String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        List<User> selectedUserList = userMapper.selectByExample(userExample);
        if(selectedUserList != null && selectedUserList.size() > 0) {
            if(selectedUserList.size() > 1){
                return true; //出现重名
            }
            if(!selectedUserList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
