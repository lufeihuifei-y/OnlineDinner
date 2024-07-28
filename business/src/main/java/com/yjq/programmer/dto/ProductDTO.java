package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-01-01 11:37
 */
public class ProductDTO {
    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="商品名称不能为空！",errorMaxLengthMsg="商品名称长度不能大于32！",errorMinLengthMsg="商品名称不能为空！")
    private String name;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999.99,minValue=0.00,errorRequiredMsg="商品价格不能为空！",errorMaxValueMsg="商品价格不能大于99999999.99元！",errorMinValueMsg="商品价格不能小于0.00元！")
    private BigDecimal price;

    private Integer viewNum;

    private Integer saleNum;

    private String photo;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=64,minLength=1,errorRequiredMsg="商品简介不能为空！",errorMaxLengthMsg="商品简介长度不能大于64！",errorMinLengthMsg="商品简介不能为空！")
    private String info;

    private Date createTime;

    @ValidateEntity(required=true,requiredMinLength=true,minLength=1,errorRequiredMsg="商品所属分类不能为空！",errorMinLengthMsg="商品所属分类不能为空！")
    private String categoryId;

    private CategoryDTO categoryDTO;

    private Integer recommend;

    private Integer state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", price=").append(price);
        sb.append(", viewNum=").append(viewNum);
        sb.append(", saleNum=").append(saleNum);
        sb.append(", photo=").append(photo);
        sb.append(", info=").append(info);
        sb.append(", createTime=").append(createTime);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", recommend=").append(recommend);
        sb.append(", categoryDTO=").append(categoryDTO);
        sb.append(", state=").append(state);
        sb.append("]");
        return sb.toString();
    }
}
