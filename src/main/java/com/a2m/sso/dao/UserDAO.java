package com.a2m.sso.dao;

import com.a2m.sso.model.UserResponse;
import com.a2m.sso.model.req.SignupReq;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

/**
 * Author tiennd
 * Created date 2023-07-08
 */

@Mapper
public interface UserDAO {
    @Cacheable("user")
    UserResponse getUserInfoByUserUid(String userUid);

    UserResponse getUserByUserId(String userId);
    
    void insertUser (UserResponse user);
    
    void insertUserInfo (UserResponse user);
    
    void setStatusByVerifyKey (String verifyKey);
    
    int checkVerifyKey(String verifyKey);
    
    int validateExistingEmail(String email);
    
    int validateExistingUserName(String username);
}
