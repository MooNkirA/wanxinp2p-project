package com.moon.shardingjdbcdemo.service;

import com.moon.shardingjdbcdemo.entity.ProductInfo;

import java.util.List;

/**
 * 商品服务
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 13:59
 * @description
 */
public interface ProductService {

    /**
     * 创建商品
     *
     * @param product 商品信息
     * @return 已创建的商品信息
     */
    void createProduct(ProductInfo product);

    /**
     * 商品列表展示
     *
     * @return 商品列表
     */
    List<ProductInfo> queryProduct();

}
