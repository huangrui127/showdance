package com.android.app.showdance.logic.event;

import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;

public class UploadDeleteEvent {
    public VideoUploadResponse videoUploadResponse;
    public int id;

    public UploadDeleteEvent(VideoUploadResponse videoUploadResponse) {
        this.videoUploadResponse = videoUploadResponse;
    }

    public UploadDeleteEvent(int id) {
        this.id = id;
    }
}
