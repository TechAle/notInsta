package com.example.mobileproject.dataLayer.sources;

public abstract class GeneralPostRemoteSource {

    protected Callback c;
    public abstract void retrievePostByDocumentId(String tag, Callback c);
    public abstract void retrievePosts(Callback c);
    public abstract void retrievePostsWithTags(String[] tags, Callback c);

    //TODO: Qua io non lo sto usando, qualcuno ne faccia qualcosa o cancellatelo
    public abstract void postPost();
}
