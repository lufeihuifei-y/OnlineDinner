package com.yjq.programmer.dao.my;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-02 17:11
 */
public interface MyCartMapper {

    // 批量删除购物车数据
    int batchDelete(@Param("idList") List<String> idList);
}
