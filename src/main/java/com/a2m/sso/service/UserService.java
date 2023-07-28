package com.a2m.sso.service;

import com.a2m.sso.model.UserResponse;
import com.a2m.sso.model.req.SignupReq;

/**
 * Author tiennd
 * Created date 2023-07-08
 */
public interface UserService {
    UserResponse getUserInfo() throws Exception;
    
    void insertUser(SignupReq signupReq) throws Exception;
    
    void forgotPass(SignupReq signupReq) throws Exception;
    
    void changeStatusByVerifyKey(String verifyKey);

	boolean checkVerifyKeyExpired(String verifyKey);

	void changePasswordByVerifyKey(SignupReq signupReq);
    
}
