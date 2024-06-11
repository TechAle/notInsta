package com.example.mobileproject.dataLayer.sources;

public abstract class GeneralWorkerSource {
    public abstract void enqueueRemoteRead();
    public abstract void enqueueRemoteWrite();
    public abstract void startFinishingDownload();
}

