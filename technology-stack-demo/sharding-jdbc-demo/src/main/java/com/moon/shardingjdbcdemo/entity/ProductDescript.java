package com.moon.shardingjdbcdemo.entity;

import lombok.Data;

/**
 * 商品描述
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 13:52
 * @description
 */
@Data
public class ProductDescript {

    private Long id;

    /**
     * 所属商品id
     */
    private Long productInfoId;

    /**
     * 商品描述
     */
    private String descript;

    /**
     * 所属店铺id
     */
    private Long storeInfoId;

}
