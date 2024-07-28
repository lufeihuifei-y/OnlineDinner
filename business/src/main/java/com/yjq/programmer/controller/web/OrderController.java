package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.OrderDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IOrderService;
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
 * @create 2022-01-04 10:16
 */
@RestController("WebOrderController")
@RequestMapping("/web/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 提交订单操作
     * @param orderDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseDTO<Boolean> addOrder(@RequestBody OrderDTO orderDTO){
        return orderService.addOrder(orderDTO);
    }

    /**
     * 获取个人订单信息
     * @param orderDTO
     * @return
     */
    @PostMapping("/self-order")
    public ResponseDTO<List<OrderDTO>> getByUserId(@RequestBody OrderDTO orderDTO){
        return orderService.getByUserId(orderDTO);
    }

    /**
     * 取消订单操作
     * @param orderDTO
     * @return
     */
    @PostMapping("/cancel")
    public ResponseDTO<Boolean> cancelOrder(@RequestBody OrderDTO orderDTO){
        return orderService.updateOrderState(orderDTO);
    }

}
