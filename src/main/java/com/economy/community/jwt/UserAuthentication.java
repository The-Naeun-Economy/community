//package com.economy.community.jwt;
//
//import lombok.Getter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//
//@Getter
//public class UserAuthentication extends AbstractAuthenticationToken {
//    private final Long userId;
//    private final String userNickname;
//
//    public UserAuthentication(Long userId, String userNickname) {
//        super(null);
//        this.userId = userId;
//        this.userNickname = userNickname;
//        setAuthenticated(true);
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this.userId;
//    }
//}
