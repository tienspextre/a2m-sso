package com.a2m.sso.controller;

import com.a2m.sso.constant.CommonConstants;
import com.a2m.sso.constant.SecurityConstants;
import com.a2m.sso.dao.UserDAO;
import com.a2m.sso.model.DataResponse;
import com.a2m.sso.model.UserResponse;
import com.a2m.sso.model.req.LoginReq;
import com.a2m.sso.model.req.SignupReq;
import com.a2m.sso.service.impl.ComSeqServiceImpl;
import com.a2m.sso.service.impl.MailServiceImpl;
import com.a2m.sso.service.impl.UserServiceImpl;
import com.a2m.sso.util.CookieUtils;
import com.a2m.sso.util.JwtProvinderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    

    @PostMapping("login")
    private ResponseEntity<DataResponse> login(@Valid @RequestBody LoginReq loginReq, HttpServletResponse response) {
        DataResponse resp = new DataResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvinderUtils.generateJwtByPrivateKey(authentication);
            CookieUtils.addCookie(response, SecurityConstants.ACCESS_TOKEN, jwt);
            resp.setStatus(CommonConstants.RESULT_OK);
            resp.setResponseData(jwt);
            resp.setMessage("Login success");
        } catch (DisabledException e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_OTP_EMAIL);
            resp.setMessage("Account isn't verified");
        } catch (Exception e) {
        	e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
            resp.setMessage("The username or password is wrong");
        }
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("signup")
    private ResponseEntity<DataResponse> signup(@Valid @RequestBody SignupReq signupReq, HttpServletResponse response) {
        DataResponse resp = new DataResponse();
        try {
//        	System.out.println(signupReq.getName());
        	userServiceImpl.insertUser(signupReq);
            resp.setStatus(CommonConstants.RESULT_OK);
            resp.setMessage("Signup success");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
            resp.setMessage("");
        }
        return ResponseEntity.ok(resp);
    }
}
