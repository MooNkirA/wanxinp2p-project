package com.moon.shardingjdbcdemo.service;

import com.moon.shardingjdbcdemo.entity.ProductDescript;
import com.moon.shardingjdbcdemo.entity.ProductInfo;
import com.moon.shardingjdbcdemo.entity.StoreInfo;
import com.moon.shardingjdbcdemo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品服务
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 14:00
 * @description
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StoreInfoService storeInfoService;

    @Override
    @Transactional
    public void createProduct(ProductInfo product) {
        ProductDescript productDescript = new ProductDescript();
        productDescript.setDescript(product.getDescript());
        productMapper.insertProductInfo(product); // 新增商品基本信息
        productDescript.setProductInfoId(product.getProductInfoId());
        productDescript.setStoreInfoId(product.getStoreInfoId()); // 冗余店铺信息
        productMapper.insertProductDescript(productDescript); // 新增商品描述信息
    }

    @Override
    public List<ProductInfo> queryProduct() {
        // 1.查询本地商品信息
        List<ProductInfo> productInfos = productMapper.selectProductList();
        Set<Long> storeIdSet = new HashSet<>();
        for (ProductInfo p : productInfos) {
            storeIdSet.add(p.getStoreInfoId());
        }
        // 2.查询并组装店铺信息
        if (storeIdSet.size() != 0) {
            Map<Integer, StoreInfo> storeMap = storeInfoService.queryStoreInfoByIds(new ArrayList<>(storeIdSet));
            for (ProductInfo p : productInfos) {
                StoreInfo storeInfo = storeMap.get(p.getStoreInfoId());
                p.setStoreName(storeInfo.getStoreName());
                p.setReputation(storeInfo.getReputation());
                p.setStoreRegionName(storeInfo.getRegionName());
            }
        }

        return productInfos;
    }

}
