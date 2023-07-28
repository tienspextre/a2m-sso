package com.a2m.sso.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.a2m.sso.dao.UserDAO;
import com.a2m.sso.model.UserResponse;
import com.a2m.sso.model.req.SignupReq;
import com.a2m.sso.service.ComSeqService;
import com.a2m.sso.service.UserService;

/**
 * Author tiennd
 * Created date 2023-07-15
 */

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserDAO userDAO;

    @Autowired
    private ComSeqService comSeqService;
    
    @Autowired
    private ComSeqServiceImpl comSeqServiceImpl = new ComSeqServiceImpl();
    
    @Autowired
    private MailServiceImpl mailServiceImpl;

    @Override
    public UserResponse getUserInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserResponse userResponse = userDetails.getUser();
        return userResponse;
    }
    
    @Override
    public void insertUser (SignupReq signupReq) throws Exception{
    	UserResponse user = new UserResponse();
		user.setUserId(signupReq.getUsername());
		user.setEmail(signupReq.getEmail());
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(signupReq.getPassword());
		user.setPwd(encodedPassword);
		user.setStatus(signupReq.getStatus());
		user.setFullName(signupReq.getName());
		user.setUserUid(comSeqServiceImpl.getSeq("seq_user_uid")); 
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    int KEY_LENGTH = 20;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(KEY_LENGTH);
        int checkAvailable = 1;
        while (checkAvailable != 0) {
        	sb = new StringBuilder(KEY_LENGTH);
            while (sb.length() < KEY_LENGTH) {
                int randomIndex = secureRandom.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }	
            checkAvailable = userDAO.checkVerifyKey(sb.toString());
        }
	    user.setVerifyKey(sb.toString());
	    LocalDateTime now = LocalDateTime.now();
	    user.setCreatedDate(now);
        userDAO.insertUser(user);
        userDAO.insertUserInfo(user);
        mailServiceImpl.sendResetPassEmail(sb.toString(), user.getEmail(), signupReq.getRedirectUri());
    }
    
    @Override
    public void forgotPass (SignupReq signupReq) throws Exception{
    	UserResponse user = new UserResponse();
		user.setUserId(signupReq.getUsername());
		user.setEmail(signupReq.getEmail());
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    int KEY_LENGTH = 20;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(KEY_LENGTH);
        int checkAvailable = 1;
        while (checkAvailable != 0) {
        	sb = new StringBuilder(KEY_LENGTH);
            while (sb.length() < KEY_LENGTH) {
                int randomIndex = secureRandom.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }	
            checkAvailable = userDAO.checkVerifyKey(sb.toString());
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredTime = now.plus(10, ChronoUnit.MINUTES);
//        System.out.println(expiredTime);
        user.setExpiredDate(expiredTime);
        user.setVerifyKey(sb.toString());
	    userDAO.updateVerifyKey(user);
        mailServiceImpl.sendResetPassEmail(sb.toString(), user.getEmail(), signupReq.getRedirectUri());
    }
    
    @Override
    public boolean checkVerifyKeyExpired(String verifyKey) {
    	LocalDateTime expiredDate = userDAO.getExpiredDateByVerifyKey(verifyKey);
    	LocalDateTime now = LocalDateTime.now();
    	if (expiredDate.compareTo(now) > 0) return true;
    	else return false;
    }
    
    @Override
    public void changeStatusByVerifyKey(String verifyKey){
    	try {
    		userDAO.setStatusByVerifyKey(verifyKey);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @Override
    public void changePasswordByVerifyKey(SignupReq signupReq) {
    	UserResponse user = new UserResponse();
    	user.setVerifyKey(signupReq.getVerifyKey());
    	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(signupReq.getPassword());
		LocalDateTime now = LocalDateTime.now();
		user.setExpiredDate(now);
    	userDAO.updatePassByVerifyKey(encodedPassword, signupReq.getVerifyKey());
    	userDAO.updateExpiredDate(user);
    }
    

    // Nếu cập nhật thông tin user thì phải xóa thông tin user đã lưu cache
    // Dùng @CacheEvict("user") tham số là userUid
    // Gọi func clearCacheUser trong CommonService

}
