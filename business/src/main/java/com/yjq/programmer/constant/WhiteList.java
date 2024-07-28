package com.yjq.programmer.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-01-02 10:09
 */
public class WhiteList {

    //登录拦截器无需拦截的url      Arrays.asList：字符串数组转化为List
    public static List<String> LoginExcludePathPatterns = Arrays.asList(
            "/admin/user/login",
            "/admin/user/check_login",
            "/web/user/check_login",
            "/web/user/login",
            "/web/user/register",
            "/web/product/recommend_list",
            "/web/product/sale_list",
            "/web/product/view_list",
            "/web/product/list",
            "/web/product/new",
            "/web/product/get",
            "/web/category/all",
            "/web/category/get",
            "/web/user/token"
    );
}
