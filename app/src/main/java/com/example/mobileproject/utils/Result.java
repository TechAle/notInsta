package com.example.mobileproject.utils;

//Qualcosa per rappresentare il risultato a livello di viewmodel

import com.example.mobileproject.models.PostResp;

public abstract class Result {
    private Result() {}
    public boolean successful(){
        return this instanceof PostResponseSuccess;
    }

    public static final class PostResponseSuccess extends Result {
        private final PostResp resp;
        public PostResponseSuccess(PostResp r) {
            this.resp = r;
        }
        public PostResp getData() {
            return resp;
        }
    }

/*    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }*/

    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
