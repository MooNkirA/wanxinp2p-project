package com.moon.ensuredemo.bank2.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInfo implements Serializable {
    private static final long serialVersionUID = -3226409355600286109L;
    private Long id;
    private String accountName;
    private String accountNo;
    private String accountPassword;
    private Double accountBalance;
}
