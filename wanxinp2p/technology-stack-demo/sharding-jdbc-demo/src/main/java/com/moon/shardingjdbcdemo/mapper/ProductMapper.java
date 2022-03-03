package com.moon.shardingjdbcdemo.mapper;

import com.moon.shardingjdbcdemo.entity.ProductDescript;
import com.moon.shardingjdbcdemo.entity.ProductInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品持久层接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 13:35
 * @description
 */
@Mapper
public interface ProductMapper {

    @Insert("insert into product_info(store_info_id,product_name,spec,region_code,price,image_url) value (#{storeInfoId},#{productName},#{spec},#{regionCode},#{price},#{imageUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "product_info_id")
    int insertProductInfo(ProductInfo productInfo);

    @Insert("insert into product_descript(product_info_id,descript,store_info_id) value (#{productInfoId},#{descript},#{storeInfoId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertProductDescript(ProductDescript productDescript);

    @Select("select i.*, d.descript from product_info i inner join product_descript d on i.product_info_id = d.product_info_id")
    List<ProductInfo> selectProductList();

}
