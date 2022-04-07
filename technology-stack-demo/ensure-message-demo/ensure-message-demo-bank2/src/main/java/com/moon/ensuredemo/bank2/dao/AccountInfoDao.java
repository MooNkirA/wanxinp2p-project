package com.moon.ensuredemo.bank2.dao;

import com.moon.ensuredemo.bank2.entity.AccountInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AccountInfoDao {
    /**
     * 增加某账号的余额
     *
     * @param accountNo 账号
     * @param amount    变动金额
     * @return
     */
    @Update("update account_info set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int addAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);


    /**
     * 查询某账号信息
     *
     * @param accountNo 账号
     * @return
     */
    @Select("select * from account_info where where account_no=#{accountNo}")
    AccountInfo findByIdAccountNo(@Param("accountNo") String accountNo);

    /**
     * 查询某事务记录是否已执行
     *
     * @param txNo 事务编号
     * @return
     */
    @Select("select count(1) from de_duplication where tx_no = #{txNo}")
    int isExistTx(long txNo);

    /**
     * 保存某事务执行记录
     *
     * @param txNo 事务编号
     * @return
     */
    @Insert("insert into de_duplication values(#{txNo},now());")
    int addTx(long txNo);
}