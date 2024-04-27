package com.paul.usercenter.model.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author paul
 */
@Setter
@Getter
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * -- SETTER --
     *  设置
     */
    private String userAccount;
    /**
     * -- GETTER --
     *  获取
     * <p>
     * -- SETTER --
     *  设置

     */
    private String userPassword;
    /**
     * -- GETTER --
     *  获取
     * <p>
     * -- SETTER --
     *  设置

     */
    private String checkPassword;

    public UserRegisterRequest() {
    }

    public UserRegisterRequest(long serialVersionUID, String userAccount, String userPassword, String checkPassword) {
//        UserRegisterRequest.serialVersionUID = serialVersionUID;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.checkPassword = checkPassword;
    }

    public String toString() {
        return "UserRegisterRequest{serialVersionUID = " + serialVersionUID + ", userAccount = " + userAccount + ", userPassword = " + userPassword + ", checkPassword = " + checkPassword + "}";
    }
}
