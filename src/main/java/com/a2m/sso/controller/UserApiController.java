package com.a2m.sso.controller;

import com.a2m.sso.constant.CommonConstants;
import com.a2m.sso.model.DataResponse;
import com.a2m.sso.model.UserResponse;
import com.a2m.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author tiennd
 * Created date 2023-07-15
 */

@RestController
@RequestMapping("api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    @GetMapping("getUserInfo")
    public ResponseEntity<UserResponse> getUserInfo() {
        DataResponse resp = new DataResponse();
        try {
            UserResponse userResponse = userService.getUserInfo();
            resp.setStatus(CommonConstants.RESULT_OK);
            resp.setResponseData(userResponse);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(CommonConstants.RESULT_NG);
        }
        return ResponseEntity.ok((UserResponse) resp.getResponseData());
    }
}
