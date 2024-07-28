package com.yjq.programmer.service;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;


public interface ICategoryService {

    // 分页获取商品分类数据
    ResponseDTO<PageDTO<CategoryDTO>> getCategoryListByPage(PageDTO<CategoryDTO> pageDTO);

    // 保存商品分类数据(添加、修改)
    ResponseDTO<Boolean> saveCategory(CategoryDTO categoryDTO);

    // 删除商品分类数据
    ResponseDTO<Boolean> removeCategory(CategoryDTO categoryDTO);

    // 获取所有商品分类数据
    ResponseDTO<List<CategoryDTO>> getAllCategoryList();

    // 前台首页获取排序靠前的6个商品分类
    ResponseDTO<List<CategoryDTO>> allCategory();

    // 根据id获取商品分类
    ResponseDTO<CategoryDTO> getById(CategoryDTO categoryDTO);

    // 获取五个成交额最高的商品分类
    ResponseDTO<List<CategoryDTO>> getCategoryListByPrice();
}
