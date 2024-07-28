package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("WebCategoryController")
@RequestMapping("/web/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 前台首页获取排序靠前的6个商品分类
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<CategoryDTO>> allCategory(){
        return categoryService.allCategory();
    }

    /**
     * 前台根据id获取商品分类
     * @param categoryDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<CategoryDTO> getById(@RequestBody CategoryDTO categoryDTO){
        return categoryService.getById(categoryDTO);
    }
}
