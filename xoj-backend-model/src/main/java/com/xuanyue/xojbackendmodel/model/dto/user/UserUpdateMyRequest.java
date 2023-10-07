package com.xuanyue.xojbackendmodel.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 *
 * @author xuanyue18
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户旧密码
     */
    private String oldUserPassword;

    /**
     * 用户新密码
     */
    private String userPassword;

    private static final long serialVersionUID = 1L;
}