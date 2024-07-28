package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.CategoryMapper;
import com.yjq.programmer.dao.ProductMapper;
import com.yjq.programmer.dao.my.MyCategoryMapper;
import com.yjq.programmer.domain.Category;
import com.yjq.programmer.domain.CategoryExample;
import com.yjq.programmer.domain.Product;
import com.yjq.programmer.domain.ProductExample;
import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICategoryService;
import com.yjq.programmer.service.IProductService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private MyCategoryMapper myCategoryMapper;

    @Resource
    private IProductService productService;

    /**
     * 分页获取商品分类数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<CategoryDTO>> getCategoryListByPage(PageDTO<CategoryDTO> pageDTO) {
        CategoryExample categoryExample = new CategoryExample();
        // 判断是否进行关键字搜索
        if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
            categoryExample.createCriteria().andNameLike("%"+pageDTO.getSearchContent()+"%");
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        categoryExample.setOrderByClause("sort desc");
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出商品分类数据
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<CategoryDTO> categoryDTOList = CopyUtil.copyList(categoryList, CategoryDTO.class);
        pageDTO.setList(categoryDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存商品分类数据(添加、修改)
     * @param categoryDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveCategory(CategoryDTO categoryDTO) {
        if(categoryDTO == null){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(categoryDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Category category = CopyUtil.copy(categoryDTO, Category.class);
        if(CommonUtil.isEmpty(category.getId())){
            // id为空 说明是添加数据
            // 判断商品分类名称是否存在
            if(isCategoryNameExist(category, "")){
                return ResponseDTO.errorByMsg(CodeMsg.CATEGORY_NAME_EXIST);
            }
            // 生产8位id
            category.setId(UuidUtil.getShortUuid());
            // 添加数据到数据库
            if(categoryMapper.insertSelective(category) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.CATEGORY_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            // 判断商品分类名称是否存在
            if(isCategoryNameExist(category, category.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.CATEGORY_NAME_EXIST);
            }
            // 修改数据库中数据
            if(categoryMapper.updateByPrimaryKeySelective(category) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.CATEGORY_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除商品分类数据
     * @param categoryDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeCategory(CategoryDTO categoryDTO) {
        if(categoryDTO == null || CommonUtil.isEmpty(categoryDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除此分类下所有商品信息
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andCategoryIdEqualTo(categoryDTO.getId());
        List<Product> productList = productMapper.selectByExample(productExample);
        List<ProductDTO> productDTOList = CopyUtil.copyList(productList, ProductDTO.class);
        for(ProductDTO productDTO : productDTOList){
            productService.removeProduct(productDTO);
        }
        // 删除商品分类信息
        if(categoryMapper.deleteByPrimaryKey(categoryDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.CATEGORY_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 获取所有商品分类数据
     * @return
     */
    @Override
    public ResponseDTO<List<CategoryDTO>> getAllCategoryList() {
        List<Category> categoryList = categoryMapper.selectByExample(new CategoryExample());
        List<CategoryDTO> categoryDTOList = CopyUtil.copyList(categoryList, CategoryDTO.class);
        return ResponseDTO.success(categoryDTOList);
    }

    /**
     * 前台首页获取排序靠前的6个商品分类
     * @return
     */
    @Override
    public ResponseDTO<List<CategoryDTO>> allCategory() {
        // 获取六条商品分类数据 按排序高到低获取
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort desc");
        PageHelper.startPage(1, 6);
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryDTO> categoryDTOList = CopyUtil.copyList(categoryList, CategoryDTO.class);

        return ResponseDTO.success(categoryDTOList);
    }

    /**
     * 根据id获取商品分类
     * @param categoryDTO
     * @return
     */
    @Override
    public ResponseDTO<CategoryDTO> getById(CategoryDTO categoryDTO) {
        if(CommonUtil.isEmpty(categoryDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 根据id获取商品分类信息
        Category category = categoryMapper.selectByPrimaryKey(categoryDTO.getId());
        return ResponseDTO.success(CopyUtil.copy(category, CategoryDTO.class));
    }

    /**
     * 获取五个成交额最高的商品分类
     * @return
     */
    @Override
    public ResponseDTO<List<CategoryDTO>> getCategoryListByPrice() {
        return ResponseDTO.success(myCategoryMapper.getCategoryListByPrice());
    }

    /**
     * 判断商品分类名称是否重复
     * @param category
     * @param id
     * @return
     */
    public Boolean isCategoryNameExist(Category category, String id) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andNameEqualTo(category.getName());
        List<Category> selectedCategoryList = categoryMapper.selectByExample(categoryExample);
        if(selectedCategoryList != null && selectedCategoryList.size() > 0) {
            if(selectedCategoryList.size() > 1){
                return true; //出现重名
            }
            if(!selectedCategoryList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }

}
