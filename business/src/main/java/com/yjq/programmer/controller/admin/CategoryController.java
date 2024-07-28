package com.yjq.programmer.controller.admin;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("AdminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 后台保存商品分类数据(添加、修改)
     * @param categoryDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.saveCategory(categoryDTO);
    }

    /**
     * 后台分页获取商品分类数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<CategoryDTO>> getCategoryListByPage(@RequestBody PageDTO<CategoryDTO> pageDTO){
        return categoryService.getCategoryListByPage(pageDTO);
    }

    /**
     * 后台删除商品分类数据
     * @param categoryDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.removeCategory(categoryDTO);
    }

    /**
     * 后台获取所有商品分类数据
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<CategoryDTO>> getAllCategoryList(){
        return categoryService.getAllCategoryList();
    }

    /**
     * 获取五个成交额最高的商品分类
     * @return
     */
    @PostMapping("/total-price")
    public ResponseDTO<List<CategoryDTO>> getCategoryListByPrice(){
        return categoryService.getCategoryListByPrice();
    }

}
