package com.yjq.programmer.service.impl;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.CartMapper;
import com.yjq.programmer.dao.ProductMapper;
import com.yjq.programmer.domain.Cart;
import com.yjq.programmer.domain.CartExample;
import com.yjq.programmer.domain.Product;
import com.yjq.programmer.dto.CartDTO;
import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICartService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-01-03 19:12
 */
@Service
@Transactional
public class CartServiceImpl implements ICartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 添加购物车操作处理
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> addCart(CartDTO cartDTO) {
        // 分空验证
        if(CommonUtil.isEmpty(cartDTO.getUserId()) || CommonUtil.isEmpty(cartDTO.getProductId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 判断该用户购物车是否已有此商品
        CartExample cartExample = new CartExample();
        cartExample.createCriteria().andUserIdEqualTo(cartDTO.getUserId()).andProductIdEqualTo(cartDTO.getProductId());
        List<Cart> cartList = cartMapper.selectByExample(cartExample);
        if(cartList == null || cartList.size() == 0){
            Cart cart = CopyUtil.copy(cartDTO, Cart.class);
            // 设置主键id
            cart.setId(UuidUtil.getShortUuid());
            // 添加购物车数据
            if(cartMapper.insertSelective(cart) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.CART_ADD_ERROR);
            }
        }else{
            // 在原有数量上添加
            Cart cart = cartList.get(0);
            cart.setQuantity(cartDTO.getQuantity() + cart.getQuantity());
            // 更新购物车数据
            if(cartMapper.updateByPrimaryKeySelective(cart) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.CART_ADD_ERROR);
            }
        }

        return ResponseDTO.successByMsg(true, "添加购物车成功！");
    }

    /**
     * 删除购物车操作处理
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteCart(CartDTO cartDTO) {
        if(CommonUtil.isEmpty(cartDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除购物车商品
        if(cartMapper.deleteByPrimaryKey(cartDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.CART_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 获取某用户的购物车商品信息
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<List<CartDTO>> listCart(CartDTO cartDTO) {
        if(CommonUtil.isEmpty(cartDTO.getUserId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        CartExample cartExample = new CartExample();
        cartExample.createCriteria().andUserIdEqualTo(cartDTO.getUserId());
        List<Cart> cartList = cartMapper.selectByExample(cartExample);
        List<CartDTO> cartDTOList = CopyUtil.copyList(cartList, CartDTO.class);
        for(CartDTO c : cartDTOList){
            Product product = productMapper.selectByPrimaryKey(c.getProductId());
            ProductDTO productDTO = CopyUtil.copy(product, ProductDTO.class);
            c.setProductDTO(productDTO);
        }
        return ResponseDTO.success(cartDTOList);
    }

    /**
     * 更新购物车中的商品数量
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateQuantity(CartDTO cartDTO) {
        if(CommonUtil.isEmpty(cartDTO.getId()) || cartDTO.getQuantity() == 0){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Cart cart = cartMapper.selectByPrimaryKey(cartDTO.getId());
        cart.setQuantity(cartDTO.getQuantity());
        if(cartMapper.updateByPrimaryKeySelective(cart) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.CART_QUANTITY_ERROR);
        }
        return ResponseDTO.success(true);
    }
}
