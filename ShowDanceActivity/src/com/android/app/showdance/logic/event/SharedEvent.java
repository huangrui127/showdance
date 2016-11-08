package com.android.app.showdance.logic.event;

import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;

public class SharedEvent{
	public VideoUploadResponse event;
	public SharedEvent(VideoUploadResponse event ) {
		this.event = event;
	}
}
