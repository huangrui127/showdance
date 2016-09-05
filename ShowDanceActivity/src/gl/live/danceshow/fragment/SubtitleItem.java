package gl.live.danceshow.fragment;

import java.io.Serializable;

import com.android.app.showdance.impl.ContentValue;
import com.android.app.showdance.model.glmodel.FontInfo.FontData;


public class SubtitleItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FontData mFontData;
	private int bDownload = ContentValue.DOWNLOAD_STATE_NONE;
	private String path;
	private String img;
	private String name;
	private Long uuid;
	public SubtitleItem() {}
	public void setData(FontData d) {
		mFontData = d;
	}
	public FontData getData() {return mFontData;}
	
	public void setbDownload(int load) {bDownload = load;}

	public int getDownload() {
		return bDownload;
	}

	public void setPath(String p) {
		path = p;
	}

	public String getPath() {
		return path;
	}

	public void setImg(String i) {
		img = i;
	}

	public String getImg() {
		if (mFontData == null)
			return img;
		else
			return mFontData.getimg();
	}

	public void setName(String n) {
		name = n;
	}

	public String getName() {
		if (mFontData == null)
			return name;
		else
			return mFontData.getname();
	}
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long id) {
		uuid=id;
	}
}