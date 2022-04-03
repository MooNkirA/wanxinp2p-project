package com.moon.shardingjdbcdemo.controller;

import com.moon.shardingjdbcdemo.entity.ProductInfo;
import com.moon.shardingjdbcdemo.service.ProductService;
import com.moon.shardingjdbcdemo.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Sharding-Jdbc 测试请求控制类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 11:27
 * @description
 */
@RestController
public class DemoController {

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private ProductService productService;

    /**
     * 查询商品列表
     *
     * @return
     */
    @GetMapping(value = "/products")
    public List<ProductInfo> queryProduct() {
        return productService.queryProduct();
    }

    /**
     * 添加商品。测试时只需要填写以下参数:
     * descript: 商品描述
     * imageUrl: 商品图片
     * price: 3
     * productName: 可乐饮料
     * regionCode: 410100
     * spec: 500ml
     * storeInfoId: 2
     *
     * @param productInfo
     * @return
     */
    @PostMapping("/products")
    public String createProject(@RequestBody ProductInfo productInfo) {
        productService.createProduct(productInfo);
        return "创建成功!";
    }

}
