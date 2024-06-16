package com.example.mobileproject.utils;

//Qualcosa per rappresentare il risultato a livello di viewmodel

import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.models.Users.UsersResp;

import java.util.List;

public abstract class Result {
    private Result() {}
    public boolean successful(){
        return !(this instanceof Error);
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

    public static final class UserResponseSuccess extends Result {
        private final UsersResp resp;
        public UserResponseSuccess(UsersResp user) {
            this.resp = user;
        }
        public UsersResp getData() {
            return resp;
        }
    }

    public static final class UserResponseSuccessUser extends Result {
        private final Users user;
        public UserResponseSuccessUser(Users user) {
            this.user = user;
        }
        public Users getData() {
            return user;
        }
    }

    public static final class UserEditSuccess extends Result {//Propongo di cambiarla con GenericSuccess
    }

    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class UserCreationSuccess extends Result {
        private final String id;
        public UserCreationSuccess(String id) {
            this.id = id;
        }
        public String getData() {
            return this.id;
        }
    }

    public static class ProductSuccess extends Result {
        private final List<Product> id;
        public ProductSuccess(List<Product> id) {
            this.id = id;
        }
        public List<Product> getData() {
            return this.id;
        }
    }
}
