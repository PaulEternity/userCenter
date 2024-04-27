package com.paul.usercenter.model.domain.request;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author paul
 */
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;
    /**
     * -- GETTER --
     *  获取
     *
     */
    @Getter
    private String userPassword;
    /**
     * -- GETTER --
     *  获取
     *
     */
    @Getter
    private String checkPassword;

    public UserRegisterRequest() {
    }

    public UserRegisterRequest(long serialVersionUID, String userAccount, String userPassword, String checkPassword) {
//        UserRegisterRequest.serialVersionUID = serialVersionUID;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.checkPassword = checkPassword;
    }

    public String getUserAccount() {
        return userAccount;
    }

    /**
     * 设置
     */
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * 设置
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * 设置
     */
    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public String toString() {
        return "UserRegisterRequest{serialVersionUID = " + serialVersionUID + ", userAccount = " + userAccount + ", userPassword = " + userPassword + ", checkPassword = " + checkPassword + "}";
    }
}
