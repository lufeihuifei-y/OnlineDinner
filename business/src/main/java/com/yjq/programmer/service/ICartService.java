package com.yjq.programmer.service;

import com.yjq.programmer.dto.CartDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;


public interface ICartService {

    // 添加购物车操作处理
    ResponseDTO<Boolean> addCart(CartDTO cartDTO);

    // 删除购物车操作处理
    ResponseDTO<Boolean> deleteCart(CartDTO cartDTO);

    // 获取某用户的购物车商品信息
    ResponseDTO<List<CartDTO>> listCart(CartDTO cartDTO);

    // 更新购物车中的商品数量
    ResponseDTO<Boolean> updateQuantity(CartDTO cartDTO);
}
