package com.pjqdyd.service;

import com.pjqdyd.pojo.User;

/**
 * @Description:  [用户的Service接口]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public interface UserService {

    /**
     * 通过openId查询用户是否存在
     * @param openId
     * @return
     */
    Boolean isUserExist(String openId);

    /**
     * 通过openId查询用户信息
     * @param openId
     * @return
     */
    User findUserByOpenId(String openId);

    /**
     * 通过用户id来查询用户信息
     * @param userId
     * @return
     */
    User findUserByUserId(String userId);

    /**
     * 保存用户
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * 通过openId和access_token来验证和保存用户信息
     * @param openId
     * @param access_token
     * @return
     */
    User verifyUserInfoAndSaveInfo(String openId, String access_token);

}
