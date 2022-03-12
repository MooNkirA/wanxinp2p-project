package com.moon.wanxinp2p.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.wanxinp2p.transaction.entity.Tender;

import java.math.BigDecimal;
import java.util.List;

/**
 * 操作投标信息表 mapper 接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-11 23:48
 * @description
 */
public interface TenderMapper extends BaseMapper<Tender> {

    /**
     * 根据标的id, 获取标的已投金额, 如果未投返回 0.0
     *
     * @param id
     * @return
     */
    List<BigDecimal> selectAmountInvestedByProjectId(Long id);

}
