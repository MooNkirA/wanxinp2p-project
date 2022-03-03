package com.moon.shardingjdbcdemo.service;

import com.moon.shardingjdbcdemo.entity.StoreInfo;
import com.moon.shardingjdbcdemo.mapper.StoreInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 店铺服务
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 14:01
 * @description
 */
@Service
public class StoreInfoServiceImpl implements StoreInfoService {

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Override
    public Map<Integer, StoreInfo> queryStoreInfoByIds(List<Long> ids) {
        return storeInfoMapper.selectStoreInfoByIds(ids);
    }

}
