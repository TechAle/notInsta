package com.example.mobileproject.utils;

//Qualcosa per rappresentare il risultato

public abstract class Result {
    private Result() {}
    public boolean successful(){
        return true;//TODO: scrivere vera funzione
    }

/*    public static final class NewsResponseSuccess extends Result {
        private final NewsResponse newsResponse;
        public NewsResponseSuccess(NewsResponse newsResponse) {
            this.newsResponse = newsResponse;
        }
        public NewsResponse getData() {
            return newsResponse;
        }
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }

    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }*/
}
