package com.a2m.sso.service;

public interface MailService {
	void sendVerifyEmail(String verifyKey, String email, String redirectUri);
}