package com.android.app.showdance.logic;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @ClassName: Task
 * @Description: 任务实体类
 * @author maminghua
 * @date 2014-12-1 上午10:44:06
 * 
 */
public class Task implements Serializable {
	/**
	 * @Fields serialVersionUID :
	 */

	private static final long serialVersionUID = 1929668529440150151L;
	private int taskID;
	private HashMap<String, Object> taskParam;

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public HashMap<String, Object> getTaskParam() {
		return taskParam;
	}

	public void setTaskParam(HashMap<String, Object> taskParam) {
		this.taskParam = taskParam;
	}

	public Task(int taskID, HashMap<String, Object> HashMap) {
		this.taskID = taskID;
		this.taskParam = HashMap;
	}

}
