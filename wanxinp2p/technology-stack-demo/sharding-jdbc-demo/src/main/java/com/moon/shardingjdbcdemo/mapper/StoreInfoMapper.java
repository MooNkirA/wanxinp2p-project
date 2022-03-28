package com.moon.shardingjdbcdemo.mapper;

import com.moon.shardingjdbcdemo.entity.StoreInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 店铺信息持久层接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 13:37
 * @description
 */
@Mapper
public interface StoreInfoMapper {

    @Select({"<script>",
            " select",
            " * ",
            " from store_info ",
            " where id in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    @MapKey("id")
    Map<Integer, StoreInfo> selectStoreInfoByIds(@Param("ids") List<Long> ids);
}
