package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.CartDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICartService;
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
 * @create 2021-11-30 16:53
 */
@RestController("WebCartController")
@RequestMapping("/web/cart")
public class CartController {

    @Resource
    private ICartService cartService;

    /**
     * 添加购物车商品操作
     * @param cartDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseDTO<Boolean> addCart(@RequestBody CartDTO cartDTO){
        return cartService.addCart(cartDTO);
    }

    /**
     * 删除购物车商品操作
     * @param cartDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteCart(@RequestBody CartDTO cartDTO){
        return cartService.deleteCart(cartDTO);
    }

    /**
     * 获取某用户购物车商品信息
     */
    @PostMapping("/list")
    public ResponseDTO<List<CartDTO>> listCart(@RequestBody CartDTO cartDTO){
        return cartService.listCart(cartDTO);
    }

    /**
     * 更新购物车中商品数量
     * @param cartDTO
     * @return
     */
    @PostMapping("/update")
    public ResponseDTO<Boolean> updateQuantity(@RequestBody CartDTO cartDTO){
        return cartService.updateQuantity(cartDTO);
    }
}
