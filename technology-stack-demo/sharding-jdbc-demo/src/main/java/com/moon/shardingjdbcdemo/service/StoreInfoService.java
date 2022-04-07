package com.moon.shardingjdbcdemo.service;

import com.moon.shardingjdbcdemo.entity.StoreInfo;

import java.util.List;
import java.util.Map;

/**
 * 店铺服务
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 13:59
 * @description
 */
public interface StoreInfoService {

    /**
     * 根据id列表获取所对应的店铺信息
     *
     * @param ids id列表
     * @return 店铺信息集合
     */
    Map<Integer, StoreInfo> queryStoreInfoByIds(List<Long> ids);

}
