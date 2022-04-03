package com.moon.hmilydemo.bank2.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * account_info 表实体
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:20
 * @description
 */
@Data
public class AccountInfo implements Serializable {

    private static final long serialVersionUID = 2377377221241080400L;
    private Long id;
    private String accountName;
    private String accountNo;
    private String accountPassword;
    private Double accountBalance;

}