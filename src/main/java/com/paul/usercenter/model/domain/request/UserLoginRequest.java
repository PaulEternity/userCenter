package com.paul.usercenter.model.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 */

@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;
    @Setter
    @Getter
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
