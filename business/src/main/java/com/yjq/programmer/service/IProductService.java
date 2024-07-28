package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;


public interface IProductService {

    // 分页获取商品数据
    ResponseDTO<PageDTO<ProductDTO>> getProductListByPage(PageDTO<ProductDTO> pageDTO);

    // 保存商品数据(添加、修改)
    ResponseDTO<Boolean> saveProduct(ProductDTO productDTO);

    // 删除商品数据
    ResponseDTO<Boolean> removeProduct(ProductDTO productDTO);

    // 前台根据商品id获取商品信息
    ResponseDTO<ProductDTO> getById(ProductDTO productDTO);

    // 首页获取全部菜单数据
    ResponseDTO<List<ProductDTO>> getProductList(ProductDTO productDTO);

    // 前台获取最新上线菜单数据
    ResponseDTO<List<ProductDTO>> listByCreateTime();

    // 获取首页推荐商品数据
    ResponseDTO<List<ProductDTO>> getRecommendList();

    // 获取首页高人气商品数据
    ResponseDTO<List<ProductDTO>> getViewList();

    // 获取首页热销商品数据
    ResponseDTO<List<ProductDTO>> getSaleList();

    // 获取商品总数
    ResponseDTO<Long> getProductTotal();

    // 获取访问量最多的三个商品
    ResponseDTO<List<ProductDTO>> getProductListByViewNum();
}
