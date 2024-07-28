package com.yjq.programmer.dao.my;

import com.yjq.programmer.domain.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-05-11 19:44
 */
public interface MyOrderItemMapper {

    // 批量插入订单详情数据
    int batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

}
