package com.android.app.showdance.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @ClassName: SerializableMap
 * @Description:
 * @author maminghua
 * @date 2015-05-13 上午09:43:51
 * 
 */
public class SerializableObj implements Serializable {

	/**
	 * @Fields serialVersionUID :
	 */

	private static final long serialVersionUID = -6176501338474051750L;

	private Map<String, Object> mediaItem;// 全局的map

	private Map<String, Object> musicItem; // 全局的map

	public Map<String, Object> getMediaItem() {
		return mediaItem;
	}

	public void setMediaItem(Map<String, Object> mediaItem) {
		this.mediaItem = mediaItem;
	}

	public Map<String, Object> getMusicItem() {
		return musicItem;
	}

	public void setMusicItem(Map<String, Object> musicItem) {
		this.musicItem = musicItem;
	}

}