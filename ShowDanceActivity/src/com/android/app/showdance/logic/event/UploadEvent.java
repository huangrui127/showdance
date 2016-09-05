package com.android.app.showdance.logic.event;

public class UploadEvent{
	public String filepath;
	public double percent;
	public String oldname;
	public String newname;
	public UploadEvent(double percent2, String filepath, String oldname,
			String newname) {
		percent = percent2;
		this.filepath = filepath;
		this.oldname = oldname;
		this.newname = newname;
	}

	@Override
	public String toString() {
		return "filepath "+" update percent "+percent+" oldname "+oldname+" newname "+newname;
	}
}
