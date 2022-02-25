package com.moon.hmilydemo.bank2.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * account_info 表持久接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:23
 * @description
 */
@Mapper
@Repository
public interface AccountInfoDao {

    @Update("update account_info set account_balance = account_balance + #{amount} where account_no = #{accountNo}")
    int addAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

    @Update("update account_info set account_balance = account_balance - #{amount} where account_no = #{accountNo}")
    int subtractAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

}
