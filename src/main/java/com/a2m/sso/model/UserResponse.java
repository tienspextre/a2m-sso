package com.a2m.sso.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author tiennd
 * Created date 2023-07-08
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 12L;

    private String userUid;
    private String userId;
    @JsonIgnore
    private String pwd;
    private LocalDateTime createdDate;
//    private String createdBy;
//    private Date pwdExpr;
//    private String updatedBy;
    private LocalDateTime updatedDate;
    private LocalDateTime expiredDate;
    private String status;
//    private Long userInfoId;
    private String email;
//    private String cellPhone;
//    private Date dob;
    private String fullName;
//    private String address;
//    private boolean gender;	
//    private String imgPath;
//    private boolean twoFAEnable;
//    @JsonIgnore
//    private String twoFAKey;
//    @JsonIgnore
    private String verifyKey;
//    private String authProvider;
//    private String organization;
//    private String position;
}
