package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxindepository.entity.DepositoryBankCard;
import com.moon.wanxindepository.entity.User;
import com.moon.wanxindepository.model.PersonalRegisterRequest;
import com.moon.wanxindepository.model.PersonalRegisterResponse;

/**
 * 存管用户信息表 服务类
 */
public interface UserService extends IService<User> {

    /**
     * 用户绑卡注册
     *
     * @param personalRegisterRequest
     * @return
     */
    PersonalRegisterResponse createUser(PersonalRegisterRequest personalRegisterRequest);

    /**
     * 根据用户编码获取绑定银行卡信息
     *
     * @param userNo
     * @return
     */
    DepositoryBankCard getDepositoryBankCardByUserNo(String userNo);

    /**
     * 校验用户交易密码
     *
     * @param userNo
     * @param password
     * @return
     */
    Boolean verifyPassword(String userNo, String password);

}
