package com.yjq.programmer.controller.admin;

import com.yjq.programmer.dto.OrderDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-04 10:43
 */
@RestController("AdminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 后台分页获取订单数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<OrderDTO>> getOrderListByPage(@RequestBody PageDTO<OrderDTO> pageDTO){
        return orderService.getOrderListByPage(pageDTO);
    }

    /**
     * 后台修改订单状态
     * @param orderDTO
     * @return
     */
    @PostMapping("/edit-state")
    public ResponseDTO<Boolean> editState(@RequestBody OrderDTO orderDTO){
        return orderService.updateOrderState(orderDTO);
    }

    /**
     * 根据订单id获取订单详情信息
     * @param orderDTO
     * @return
     */
    @PostMapping("/order-item")
    public ResponseDTO<OrderDTO> getOrderItemByOrderId(@RequestBody OrderDTO orderDTO){
        return orderService.getOrderItemByOrderId(orderDTO);
    }

    /**
     * 后台删除订单数据
     * @param orderDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeOrder(@RequestBody OrderDTO orderDTO){
        return orderService.removeOrder(orderDTO);
    }

    /**
     * 后台获取订单总数
     * @return
     */
    @PostMapping("/total")
    public ResponseDTO<Long> getOrderTotal(){
        return orderService.getOrderTotal();
    }

    /**
     * 获取今日订单成交金额
     * @return
     */
    @PostMapping("/today-price")
    public ResponseDTO<BigDecimal> getTodayPrice(){
        return orderService.getTodayPrice();
    }

    /**
     * 获取本周订单成交金额
     * @return
     */
    @PostMapping("/week-price")
    public ResponseDTO<BigDecimal> getWeekPrice(){
        return orderService.getWeekPrice();
    }

    /**
     * 获取本月订单成交金额
     * @return
     */
    @PostMapping("/month-price")
    public ResponseDTO<BigDecimal> getMonthPrice(){
        return orderService.getMonthPrice();
    }

    /**
     * 根据时间范围和订单状态获取交易的订单总数
     * @return
     */
    @PostMapping("/count-state-date")
    public ResponseDTO<List<Integer>> getOrderCountByDateAndState(){
        return orderService.getOrderCountByDateAndState();
    }

}
