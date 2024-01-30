package com.example.mobileproject.dataLayer.sources;

public abstract class GeneralPostRemoteSource {

    protected CallbackPosts c;
    public abstract void retrievePostByDocumentId(String tag, CallbackPosts c);
    public abstract void retrievePosts(CallbackPosts c);
    public abstract void retrievePostsWithTags(String[] tags, CallbackPosts c);

    //TODO: Qua io non lo sto usando, qualcuno ne faccia qualcosa o cancellatelo
    public abstract void postPost();
}
