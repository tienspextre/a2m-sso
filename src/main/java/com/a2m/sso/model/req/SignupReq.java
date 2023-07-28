package com.a2m.sso.model.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupReq {
    private String username;
    private String password;
    private String email;
    private String name;
    private int userUID;
    private String status = "02-04";
    private String redirectUri;
    private String verifyKey;
    private Date createdDate;
    private Date updatedDate;
}
