package com.moon.ensuredemo.bank1.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInfo implements Serializable {
    private static final long serialVersionUID = 8278507161040726901L;
    private Long id;
    private String accountName;
    private String accountNo;
    private String accountPassword;
    private Double accountBalance;
}
