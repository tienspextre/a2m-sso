package com.a2m.sso.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2m.sso.constant.CommonConstants;
import com.a2m.sso.constant.SecurityConstants;
import com.a2m.sso.dao.UserDAO;
import com.a2m.sso.model.DataResponse;
import com.a2m.sso.model.UserResponse;
import com.a2m.sso.model.req.LoginReq;
import com.a2m.sso.model.req.SignupReq;
import com.a2m.sso.service.impl.UserServiceImpl;
import com.a2m.sso.util.CookieUtils;
import com.a2m.sso.util.JwtProvinderUtils;

/**
 * Author tiennd
 * Created date 2023-07-08
 */

@RestController
@RequestMapping("api/auth")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvinderUtils jwtProvinderUtils;
    
    @Autowired
    private UserServiceImpl userServiceImpl;
    
    @Autowired
    private UserDAO userDAO;

    @PostMapping("login")
    private ResponseEntity<DataResponse> login(@Valid @RequestBody LoginReq loginReq, HttpServletResponse response) {
        DataResponse resp = new DataResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
//            String status = userServiceImpl.getStatusByUserID(loginReq.getUsername());
//            System.out.println(status);
//            if (status.equals("02-04")) {
//            	resp.setStatus(CommonConstants.RESULT_OTP_EMAIL);
//            	resp.setMessage("Account isn't verified");
//            }
//            else {
            	SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtProvinderUtils.generateJwtByPrivateKey(authentication);
                CookieUtils.addCookie(response, SecurityConstants.ACCESS_TOKEN, jwt);
                resp.setStatus(CommonConstants.RESULT_OK);
                resp.setResponseData(jwt);
                resp.setMessage("Login success");
//            }
            
        }
        catch(BadCredentialsException e) {
        	e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
        	resp.setMessage("Username or password error");
        }
        catch (DisabledException e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_OTP_EMAIL);
        	resp.setMessage("Username disabled");
        } 
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("signup")
    private ResponseEntity<DataResponse> signup(@Valid @RequestBody SignupReq signupReq, HttpServletResponse response) {
        DataResponse resp = new DataResponse();
        try {
//        	System.out.println(signupReq.getName());
        	if (userDAO.validateExistingUserName(signupReq.getUsername()) != 0) {
        		resp.setStatus(CommonConstants.USER_EXISTED);
                resp.setMessage("Username existed");
        	}
        	else if (userDAO.validateExistingEmail(signupReq.getEmail()) != 0) {
        		resp.setStatus(CommonConstants.EMAIL_EXISTED);
                resp.setMessage("Email existed");
        	}
        	else {
        		userServiceImpl.insertUser(signupReq);
        		resp.setStatus(CommonConstants.RESULT_OK);
        		resp.setMessage("Signup success");
        	}
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
            resp.setMessage("");
        }
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("forgot")
    private ResponseEntity<DataResponse> forgot(@Valid @RequestBody SignupReq signupReq, HttpServletResponse response) {
        DataResponse resp = new DataResponse();
        try {
//        	System.out.println(signupReq.getEmail());
        	if (userDAO.checkUserByEmail(signupReq.getUsername(), signupReq.getEmail()) == 1 && userDAO.getStatusByUserId(signupReq.getUsername()).equals("02-03")) {
        		userServiceImpl.forgotPass(signupReq);
        		System.out.println("OK");
        		resp.setStatus(CommonConstants.RESULT_OK);
        		resp.setMessage("");
        	}
        	else if (userDAO.getStatusByUserId(signupReq.getUsername()) != "02-03") {
        		resp.setStatus(CommonConstants.RESULT_NP);
                resp.setMessage("");
        	}
        	else{
        		resp.setStatus(CommonConstants.RESULT_NG);
                resp.setMessage("");
        	}
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
            resp.setMessage("");
        }
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("reset")
    private ResponseEntity<DataResponse> resetPass(@Valid @RequestBody SignupReq signupReq, HttpServletResponse response) {
        DataResponse resp = new DataResponse();
        try {
        	userServiceImpl.changePasswordByVerifyKey(signupReq);
        	resp.setStatus(CommonConstants.RESULT_OK);
            resp.setMessage("");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
            resp.setMessage("");
        }
        return ResponseEntity.ok(resp);
    }
}
