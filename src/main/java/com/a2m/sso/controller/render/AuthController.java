package com.a2m.sso.controller.render;

import com.a2m.sso.constant.CommonConstants;
import com.a2m.sso.constant.SecurityConstants;
import com.a2m.sso.dao.UserDAO;
import com.a2m.sso.model.UserResponse;
import com.a2m.sso.service.impl.UserDetailsServiceImpl;
import com.a2m.sso.service.impl.UserServiceImpl;
import com.a2m.sso.util.CookieUtils;
import com.a2m.sso.util.JwtProvinderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author tiennd
 * Created date 2023-07-09
 */

@Controller
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private JwtProvinderUtils jwtProvinderUtils;
    
    @Autowired
    private UserServiceImpl userServiceImpl;
    
    @Autowired
    private UserDAO userDAO;

    @GetMapping("login")
    public String login(@RequestParam String redirectUri, @RequestParam(required = false) String actionType, HttpServletRequest request, Model model, HttpServletResponse response) {

        model.addAttribute(SecurityConstants.REDIRECT_URI_KEY, redirectUri);

        if (CommonConstants.ACTION_LOGOUT_TYPE.equals(actionType)) {
            CookieUtils.deleteCookieByName(SecurityConstants.ACCESS_TOKEN, request, response);
            return "login/login";
        }

        String accessToken = CookieUtils.getCookieByName(SecurityConstants.ACCESS_TOKEN, request);
        try {
            if (accessToken != null && jwtProvinderUtils.validateJwtToken(accessToken) && !jwtProvinderUtils.hasTokenExpired(accessToken)) {
                String url = redirectUri + "?" + SecurityConstants.ACCESS_TOKEN + "=" + accessToken;
                return "redirect:" + url;
            } else {
                return "login/login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            CookieUtils.deleteCookieByName(SecurityConstants.ACCESS_TOKEN, request, response);
            return "login/login";
        }
    }

    @GetMapping("sign-up")
    public String login(@RequestParam String redirectUri, Model model) {
        model.addAttribute(SecurityConstants.REDIRECT_URI_KEY, redirectUri);
        return "sign-up/sign-up";
    }
    @GetMapping("verify")
    public String verify(@RequestParam String verifyKey, @RequestParam String redirectUri, Model model) {
    	model.addAttribute(SecurityConstants.REDIRECT_URI_KEY, redirectUri);
    	int count = userDAO.checkVerifyKey(verifyKey);
    	if (count != 0) {
    		userServiceImpl.changeStatusByVerifyKey(verifyKey);
    	}
    	else return "/verify/verifyFailed";
    	return "verify/verify";
    }
    
    @GetMapping("forgot")
    public String forgotPass(@RequestParam String redirectUri, Model model) {
    	model.addAttribute(SecurityConstants.REDIRECT_URI_KEY, redirectUri);
    	return "forgotPass/forgotPass";
//    	int count = userDAO.checkVerifyKey(verifyKey);
//    	if (count != 0) {
//    		return "login/forgot";
//    	}
//    	return "/login/verifyFailed";
    }
    
    @GetMapping("reset")
    public String resetPass(@RequestParam String verifyKey, @RequestParam String redirectUri, Model model) {
    	model.addAttribute(SecurityConstants.REDIRECT_URI_KEY, redirectUri);
    	int count = userDAO.checkVerifyKey(verifyKey);
    	if (count != 0) {
    		return "forgotPass/resetPass";
    	}
    	return "/forgotPass/falseKey";
    }
    

}
