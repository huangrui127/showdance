package com.android.app.showdance.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.app.showdance.entity.Keys;
import com.android.app.showdance.model.DownloadMedia;
import com.android.app.showdance.model.DownloadMediaPageVo;
import com.android.app.showdance.model.DownloadMusic;
import com.android.app.showdance.model.DownloadMusicInfo;
import com.android.app.showdance.model.DownloadMusicPageVo;
import com.android.app.showdance.model.MediaComment;
import com.android.app.showdance.model.MediaCommentPageVo;
import com.android.app.showdance.model.MediaInfo;
import com.android.app.showdance.model.MediaInfoPageVo;
import com.android.app.showdance.model.MemberFeedback;
import com.android.app.showdance.model.Praise;
import com.android.app.showdance.model.ShareInfo;
import com.android.app.showdance.model.TeacherDancerMusic;
import com.android.app.showdance.model.User;
import com.android.app.showdance.model.UserFans;
import com.android.app.showdance.model.UserFansPageVo;
import com.android.app.showdance.model.UserInfo;
import com.android.app.showdance.model.UserVo;
import com.android.app.showdance.model.VDanceMusicPageVo;
import com.android.app.showdance.model.VMediaInfo;
import com.android.app.showdance.model.VUserFansMediaMusicCount;
import com.android.app.wumeiniang.app.InitApplication;

import android.content.Context;

/**
 * 
 * @ClassName: SoapWebServiceInterface
 * @Description: WebService数据传输接口类
 * 
 */
public class SoapWebServiceInterface {
	/**
	 * @Description:将UserInfo对象转换成JSON对象
	 * @param mObject
	 * @param @return
	 * @return JSONObject
	 */
	private static JSONObject userInfoToJson(Object mObject) {
		JSONObject json = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String result = objectMapper.writeValueAsString(mObject);
			json = new JSONObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * * @Description:将List集合转换成json对象
	 * 
	 * @param mObject
	 * @return
	 */

	public static JSONArray listToJson(Object mObject) {
		JSONArray json = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String result = objectMapper.writeValueAsString(mObject);
			json = new JSONArray(result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * @Description:1、【获取验证码】 (有3个地方用到：注册-获取验证码、修改手机号-获取验证码、找回密码-获取验证码)
	 * @param context
	 * @param mUserVo
	 * @param @return
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> GetcodeToJson(UserVo mUserVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserVo);
		try {
			map = Getcode(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @Description:1、【获取验证码】接口 (有3个地方用到：注册-获取验证码、修改手机号-获取验证码、找回密码-获取验证码)
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> Getcode(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.checkPhone, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);
		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**** 获取验证码 —— 结束 ****/

	/**
	 * 2、校验验证码 CheckcodeToJson (有3个地方用到：注册-校验验证码、修改手机号-校验验证码、找回密码-校验验证码)
	 */
	public static Map<String, Object> CheckcodeToJson(UserVo mUserVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserVo);
		try {
			map = Checkcode(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> Checkcode(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.checkCode, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**** 校验验证码 —— 结束 ****/

	/**
	 * 3、注册 registerUserToJson
	 */
	public static Map<String, Object> registerUserToJson(UserVo mUserVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserVo);
		try {
			map = registerUser(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> registerUser(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.registerUser, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**** 注册 —— 结束 ****/

	/**
	 * 4、登录 loginUserToJson
	 */
	public static Map<String, Object> loginUserToJson(User mUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUser);
		try {
			map = loginUser(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> loginUser(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.login, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				map.put("result", result);
				// 将Json字符串转换成,对象
				UserInfo userInfo = JsonUtil.convertToObject(UserInfo.class, mJsonObject.getString("resultInfo"));
				// 将用户信息userInfo 存储在配置文件中
				InitApplication.mSpUtil.setUserInfo(userInfo);

				String id = mJsonObject.getJSONObject("resultInfo").getString("id");
				String token = mJsonObject.getJSONObject("resultInfo").getString("token");
				userInfo.setId(Long.parseLong(id));
				userInfo.setToken(token);

			} else { // result为1或2
				map.put("result", result);
				map.put("failInfo", mJsonObject.getString("failInfo"));
			}
		} else {
			map.put("result", -1);
		}

		return map;
	}

	/**** 登录 —— 结束 ****/

	/**
	 * 5、修改密码 modifyPWDToJson
	 */
	public static Map<String, Object> modifyPWDToJson(UserVo mUserVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserVo);
		try {
			map = modifyPWD(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> modifyPWD(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.modifyPwd, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				map.put("result", result);
			} else { // result为1或2
				map.put("result", result);
				map.put("failInfo", mJsonObject.getString("failInfo"));
			}
		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**** 修改密码 —— 结束 ****/

	/**
	 * 6、重置密码 forgetPwd_ModifyPwdToJson
	 */
	public static Map<String, Object> forgetPwd_ModifyPwdToJson(UserVo mUserVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserVo);
		try {
			map = forgetPwd_ModifyPwd(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> forgetPwd_ModifyPwd(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.modifyPwdByMobilephone, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**** 重置密码 —— 结束 ****/

	/**
	 * 8、修改用户信息 modifyUserInfoToJson
	 */
	public static Map<String, Object> modifyUserInfoToJson(UserInfo mUserInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserInfo);
		try {
			map = modifyUserInfo(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> modifyUserInfo(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.modifyUser, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				map.put("result", result);
				// 将Json字符串转换成--UserInfo对象
				UserInfo userInfo = JsonUtil.convertToObject(UserInfo.class, mJsonObject.getString("resultInfo"));
				// 将用户信息userInfo 存储在配置文件中
				InitApplication.mSpUtil.setUserInfo(userInfo);
			} else { // result为1或2
				map.put("result", result);
				// map.put("failInfo", mJsonObject.getString("failInfo"));
			}
		} else {
			map.put("result", -1);
		}
		return map;

		/**** 8 修改用户信息 —— 结束 ****/
	}

	/**
	 * 
	 * @Description:【修改登录用户所在城市】
	 * @param context
	 * @param user
	 * @param @return
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> modifyRegionalToJson(User user) {
		Map<String, Object> modifyMap = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(user);
		try {
			modifyMap = modifyRegional(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return modifyMap;
	}

	/**
	 * 
	 * @Description:【修改登录用户所在城市】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> modifyRegional(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> modifyMap = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.modifyRegional, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			modifyMap.put("result", result);
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				modifyMap.put("resultInfo", resultInfo);
			}
		}
		return modifyMap;
	}

	/**
	 * 
	 * @Description:【下载推荐舞曲】
	 * @param context
	 * @param danceMusicPageVo
	 * @param @return
	 * @return ArrayList<Map<String,Object>>
	 */
	public static List<DownloadMusicInfo> danceMusicPageVoToJson(VDanceMusicPageVo danceMusicPageVo) {
		List<DownloadMusicInfo> list = new ArrayList<DownloadMusicInfo>();
		JSONObject mJSONObject = userInfoToJson(danceMusicPageVo);
		try {
			list = getVDanceMusicByName(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Description:【下载推荐舞曲】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return ArrayList<Map<String,Object>>
	 */
	public static List<DownloadMusicInfo> getVDanceMusicByName(JSONObject mJSONObject) throws JSONException {
		return null;
	}

	/**
	 * 
	 * @Description:【保存下载舞曲记录】
	 * @param context
	 * @param downloadMusic
	 * @param @return
	 * @return ArrayList<Map<String,Object>>
	 */
	public static Map<String, Object> saveDownloadMusicToJson(DownloadMusic downloadMusic) {
		Map<String, Object> downMap = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(downloadMusic);
		try {
			downMap = saveDownloadMusic(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return downMap;
	}

	/**
	 * 
	 * @Description:【保存下载舞曲记录】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return ArrayList<Map<String,Object>>
	 */
	public static Map<String, Object> saveDownloadMusic(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> downMap = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.saveDownloadMusic, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				downMap.put("result", result);
			}
		}
		return downMap;
	}

	/**
	 * 
	 * @Description:100【删除"我的视频"】Json
	 * @param context
	 * @param deleteMediaInfo
	 * @param @return
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> deleteMediaInfoToJson(List<MediaInfo> deleteMediaInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray orderJSONObject = listToJson(deleteMediaInfo);
		try {
			map = deleteMediaInfo(orderJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @Description:【删除"我的视频"】接口
	 * @param context
	 * @param orderJSONArray
	 * @param @return
	 * @param @throws JSONException
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> deleteMediaInfo(JSONArray orderJSONArray) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.deleteMediaInfo, orderJSONArray);
		String result = null;
		if (jsonData != null) {

			if (jsonData != null) {
				JSONObject mJsonObject = new JSONObject(jsonData);
				result = mJsonObject.getString("result");
				map.put("result", result);
			} else {
				map.put("result", -1);
			}

		}
		return map;
	}

	/**
	 * 101 获得“我的视频” (即我的秀舞)
	 */
	public static ArrayList<Map<String, Object>> MediaInfoPageVoToJson(MediaInfoPageVo mediaInfoPageVo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(mediaInfoPageVo);
		try {
			list = getMyVedio(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Map<String, Object>> getMyVedio(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getMediaInfoPageByCreateUser, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();
				String pageNo = mJsonObjectResult.getString("pageNo");
				String pageSize = mJsonObjectResult.getString("pageSize");
				String autoCount = mJsonObjectResult.getString("autoCount");
				String totalCount = mJsonObjectResult.getString("totalCount");
				String totalPage = mJsonObjectResult.getString("totalPage");
				String firstPage = mJsonObjectResult.getString("firstPage");
				String lastPage = mJsonObjectResult.getString("lastPage");

				pageMap.put("pageNo", pageNo);
				pageMap.put("pageSize", pageSize);
				pageMap.put("autoCount", autoCount);
				pageMap.put("totalCount", totalCount);
				pageMap.put("totalPage", totalPage);
				pageMap.put("firstPage", firstPage);
				pageMap.put("lastPage", lastPage);
				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> dmap = new HashMap<String, Object>();
					dmap.put("mediaId", ((JSONObject) array.get(j)).getString("id"));
					dmap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					dmap.put("danceMusicId", ((JSONObject) array.get(j)).getString("danceMusicId"));
					dmap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));
					dmap.put("remark", ((JSONObject) array.get(j)).getString("remark"));// 名称
					dmap.put("snapshot", ((JSONObject) array.get(j)).getString("snapshot"));
					dmap.put("mediaNewName", ((JSONObject) array.get(j)).getString("mediaNewName"));
					dmap.put("mediaOldName", ((JSONObject) array.get(j)).getString("mediaOldName"));
					dmap.put("danceTeamId", ((JSONObject) array.get(j)).getString("danceTeamId"));

					dmap.put("musicAuthor", ((JSONObject) array.get(j)).getString("musicAuthor"));
					dmap.put("musicName", ((JSONObject) array.get(j)).getString("musicName"));

					list.add(dmap);
				}
				// map.put("dataList", dataList);

			}

		}
		return list;
	}

	/**** “我的视频” —— 结束 ****/

	/**
	 * 102 获得“我关注的”
	 */
	public static ArrayList<Map<String, Object>> myAttentionPageVoToJson(UserFansPageVo userFansPageVo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(userFansPageVo);
		try {
			list = getMyAttention(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Map<String, Object>> getMyAttention(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getUserFansPagesByCreateUser, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();
				String pageNo = mJsonObjectResult.getString("pageNo");
				String pageSize = mJsonObjectResult.getString("pageSize");
				String autoCount = mJsonObjectResult.getString("autoCount");
				String totalCount = mJsonObjectResult.getString("totalCount");
				String totalPage = mJsonObjectResult.getString("totalPage");
				String firstPage = mJsonObjectResult.getString("firstPage");
				String lastPage = mJsonObjectResult.getString("lastPage");

				pageMap.put("pageNo", pageNo);
				pageMap.put("pageSize", pageSize);
				pageMap.put("autoCount", autoCount);
				pageMap.put("totalCount", totalCount);
				pageMap.put("totalPage", totalPage);
				pageMap.put("firstPage", firstPage);
				pageMap.put("lastPage", lastPage);

				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> cmap = new HashMap<String, Object>();
					// cmap.put("id", ((JSONObject) array.get(j)).getInt("id"));
					cmap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					cmap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));
					cmap.put("userId", ((JSONObject) array.get(j)).getString("userId"));
					cmap.put("name", ((JSONObject) array.get(j)).getString("name"));
					cmap.put("photo", ((JSONObject) array.get(j)).getString("photo"));

					list.add(cmap);
				}

			}
		}
		return list;
	}

	/**** 102 获得“我关注的” —— 结束 ****/

	/**
	 * 103 获得“我的粉丝”
	 */
	public static ArrayList<Map<String, Object>> UserFansPageVoToJson(UserFansPageVo userFansPageVo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(userFansPageVo);
		try {
			list = getMyFans(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Map<String, Object>> getMyFans(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getUserFansPagesByUserId, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();
				String pageNo = mJsonObjectResult.getString("pageNo");
				String pageSize = mJsonObjectResult.getString("pageSize");
				String autoCount = mJsonObjectResult.getString("autoCount");
				String totalCount = mJsonObjectResult.getString("totalCount");
				String totalPage = mJsonObjectResult.getString("totalPage");
				String firstPage = mJsonObjectResult.getString("firstPage");
				String lastPage = mJsonObjectResult.getString("lastPage");

				pageMap.put("pageNo", pageNo);
				pageMap.put("pageSize", pageSize);
				pageMap.put("autoCount", autoCount);
				pageMap.put("totalCount", totalCount);
				pageMap.put("totalPage", totalPage);
				pageMap.put("firstPage", firstPage);
				pageMap.put("lastPage", lastPage);

				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> cmap = new HashMap<String, Object>();
					// cmap.put("id", ((JSONObject) array.get(j)).getInt("id"));
					cmap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					cmap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));
					cmap.put("userId", ((JSONObject) array.get(j)).getString("userId"));
					cmap.put("name", ((JSONObject) array.get(j)).getString("name"));
					cmap.put("photo", ((JSONObject) array.get(j)).getString("photo"));

					list.add(cmap);
				}

			}
		}
		return list;
	}

	/**** 103 获得“我的粉丝” —— 结束 ****/

	/**
	 * 104 我的下载（我下载的视频）
	 */
	public static ArrayList<Map<String, Object>> downloadMediaPageVoToJson(DownloadMediaPageVo vo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(vo);
		try {
			list = getMyDownAVFans(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Map<String, Object>> getMyDownAVFans(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getDownloadMediaPagesByCreateUser, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();
				String pageNo = mJsonObjectResult.getString("pageNo");
				String pageSize = mJsonObjectResult.getString("pageSize");
				String autoCount = mJsonObjectResult.getString("autoCount");
				String totalCount = mJsonObjectResult.getString("totalCount");
				String totalPage = mJsonObjectResult.getString("totalPage");
				String firstPage = mJsonObjectResult.getString("firstPage");
				String lastPage = mJsonObjectResult.getString("lastPage");

				pageMap.put("pageNo", pageNo);
				pageMap.put("pageSize", pageSize);
				pageMap.put("autoCount", autoCount);
				pageMap.put("totalCount", totalCount);
				pageMap.put("totalPage", totalPage);
				pageMap.put("firstPage", firstPage);
				pageMap.put("lastPage", lastPage);
				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> cmap = new HashMap<String, Object>();
					cmap.put("id", ((JSONObject) array.get(j)).getInt("id"));
					cmap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					cmap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));// 我
					cmap.put("mediaNewName", ((JSONObject) array.get(j)).getString("mediaNewName"));
					cmap.put("mediaOldName", ((JSONObject) array.get(j)).getString("mediaOldName"));
					cmap.put("userId", ((JSONObject) array.get(j)).getInt("userId"));// 视频发布者
					cmap.put("mediaId", ((JSONObject) array.get(j)).getInt("mediaId"));
					cmap.put("snapshot", ((JSONObject) array.get(j)).getString("snapshot"));

					cmap.put("loginName", ((JSONObject) array.get(j)).getString("loginName"));
					cmap.put("remark", ((JSONObject) array.get(j)).getString("remark"));
					list.add(cmap);
				}
			}

		}
		return list;
	}

	/**** 104 我的下载（我下载的视频） —— 结束 ****/

	/**
	 * 105 根据用户id，查询用户详情
	 */
	public static Map<String, Object> getNearManDetailToJson(User mUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUser);
		try {
			map = getUserInfoById(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> getUserInfoById(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getUserInfoById, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				map.put("result", result);
				// 将Json字符串转换成--UserInfo对象
				User user = JsonUtil.convertToObject(User.class, mJsonObject.getString("resultInfo"));

				// 将用户信息userInfo 存储在配置文件中
				map.put("loginName", user.getLoginName());
				map.put("name", user.getName());
				map.put("sex", user.getSex());
				map.put("birthday", user.getBirthday());
				map.put("photo", user.getPhoto());
				map.put("address", user.getAddress());
				map.put("signature", user.getSignature());
				map.put("mobilephone", user.getMobilephone());
				map.put("wallpaper", user.getWallpaper());
				map.put("id", user.getId());

			} else { // result为1或2
				map.put("result", result);
			}
		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**
	 * 106 通过视频id，查询视频详情和个人相关详情getMediaInfoById
	 */
	public static Map<String, Object> getMediaInfoByIdToJson(MediaInfoPageVo mMediaInfoPageVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mMediaInfoPageVo);
		try {
			map = getMediaInfoById(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> getMediaInfoById(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getMediaInfoById, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				result = mJsonObject.getString("result");
				if (result.equals("0")) {
					map.put("result", result);
					// 将Json字符串转换成--UserInfo对象
					if (mJsonObject.getString("resultInfo") != null && mJsonObject.getString("resultInfo") != "" && mJsonObject.getString("resultInfo") != "null") {
						VMediaInfo mVMediaInfo = JsonUtil.convertToObject(VMediaInfo.class, mJsonObject.getString("resultInfo"));

						map.put("name", mVMediaInfo.getName());
						map.put("praiseSum", mVMediaInfo.getPraiseSum());
						map.put("downloadMediaSum", mVMediaInfo.getDownloadMediaSum());
						map.put("userFansCount", mVMediaInfo.getUserFansCount());// 粉丝数
						map.put("address", mVMediaInfo.getAddress());
						map.put("signature", mVMediaInfo.getSignature());
						map.put("photo", mVMediaInfo.getPhoto());
						map.put("mediaCommentCount", mVMediaInfo.getMediaCommentCount());
						map.put("shareMediaCount", mVMediaInfo.getShareMediaCount());
						map.put("mediaOldName", mVMediaInfo.getMediaOldName());
						map.put("mediaNewName", mVMediaInfo.getMediaNewName());
						map.put("snapshot", mVMediaInfo.getSnapshot());
						map.put("remark", mVMediaInfo.getRemark());
						map.put("flag", mVMediaInfo.getFlag());
						map.put("playCount", mVMediaInfo.getPlayCount());
						map.put("createUser", mVMediaInfo.getCreateUser());
						map.put("danceMusicId", mVMediaInfo.getDanceMusicId());
						map.put("createTime", mVMediaInfo.getCreateTime());
						map.put("id", mVMediaInfo.getId());
						map.put("token", mVMediaInfo.getToken());

						map.put("noValue", 0);
					} else {

						map.put("noValue", 1);
					}

				} else { // result为1或2
					map.put("result", result);
				}
			}

		}
		return map;
	}

	/**
	 * 107 根据用户id，获取关注舞友、粉丝、下载视频、我的秀舞 数量
	 */
	public static Map<String, Object> getVUserFansMediaMusicCountByIdToJson(VUserFansMediaMusicCount mVUserFansMediaMusicCount) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mVUserFansMediaMusicCount);
		try {
			map = getVUserFansMediaMusicCountById(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> getVUserFansMediaMusicCountById(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getVUserFansMediaMusicCountById, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				result = mJsonObject.getString("result");
				if (result.equals("0")) {
					map.put("result", result);
					// 将Json字符串转换成--UserInfo对象
					if (mJsonObject.getString("resultInfo") != null && mJsonObject.getString("resultInfo") != "" && mJsonObject.getString("resultInfo") != "null") {
						VUserFansMediaMusicCount mVUserFansMediaMusicCount = JsonUtil.convertToObject(VUserFansMediaMusicCount.class, mJsonObject.getString("resultInfo"));

						// 将用户信息userInfo 存储在配置文件中
						map.put("downloadMediaCount", mVUserFansMediaMusicCount.getDownloadMediaCount());
						map.put("mediaInfoCount", mVUserFansMediaMusicCount.getMediaInfoCount());
						map.put("userFansCount", mVUserFansMediaMusicCount.getUserFansCount());
						map.put("createUserFansCount", mVUserFansMediaMusicCount.getCreateUserFansCount());
						map.put("downloadMusicCount", mVUserFansMediaMusicCount.getDownloadMusicCount());// 用户ID
						map.put("id", mVUserFansMediaMusicCount.getId());

						map.put("noValue", 0);
					} else {

						map.put("noValue", 1);
					}

				} else { // result为1或2
					map.put("result", result);
				}
			}

		}
		return map;
	}

	/**
	 * 108 关注用户
	 */
	public static Map<String, Object> saveUserFansToJson(UserFans mUserFans) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserFans);
		try {
			map = saveUserFans(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> saveUserFans(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.saveUserFans, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**
	 * 109取消关注用户
	 */
	public static Map<String, Object> deleteUserFansToJson(UserFans mUserFans) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mUserFans);
		try {
			map = deleteUserFans(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> deleteUserFans(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.deleteUserFans, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**
	 * 110 保存下载视频记录
	 */
	public static Map<String, Object> saveDownloadMediaToJson(DownloadMedia mDownloadMedia) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mDownloadMedia);
		try {
			map = saveDownloadMedia(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> saveDownloadMedia(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.saveDownloadMedia, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**
	 * 111 点赞操作
	 */
	public static Map<String, Object> savePraiseToJson(Praise mPraise) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mPraise);
		try {
			map = savePraise(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> savePraise(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.savePraise, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**
	 * 112 分享
	 */
	public static Map<String, Object> saveShareInfoToJson(ShareInfo mShareInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mShareInfo);
		try {
			map = saveShareInfo(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> saveShareInfo(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.saveShareInfo, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			map.put("result", result);

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/**
	 * 113 获得视频评论
	 */
	public static ArrayList<Map<String, Object>> getMediaCommentListByMediaIdToJson(MediaCommentPageVo mMediaCommentPageVo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(mMediaCommentPageVo);
		try {
			list = getMediaCommentListByMediaId(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Map<String, Object>> getMediaCommentListByMediaId(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getMediaCommentListByMediaId, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();
				String pageNo = mJsonObjectResult.getString("pageNo");
				String pageSize = mJsonObjectResult.getString("pageSize");
				String autoCount = mJsonObjectResult.getString("autoCount");
				String totalCount = mJsonObjectResult.getString("totalCount");
				String totalPage = mJsonObjectResult.getString("totalPage");
				String firstPage = mJsonObjectResult.getString("firstPage");
				String lastPage = mJsonObjectResult.getString("lastPage");

				pageMap.put("pageNo", pageNo);
				pageMap.put("pageSize", pageSize);
				pageMap.put("autoCount", autoCount);
				pageMap.put("totalCount", totalCount);
				pageMap.put("totalPage", totalPage);
				pageMap.put("firstPage", firstPage);
				pageMap.put("lastPage", lastPage);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				String commentNum = String.valueOf(array.length()); // 评论数量

				pageMap.put("commentNum", commentNum);
				list.add(0, pageMap);

				Date createTimeDate = null;
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> cmap = new HashMap<String, Object>();
					// cmap.put("id", ((JSONObject) array.get(j)).getInt("id"));

					// String createTime = ((JSONObject)
					// array.get(j)).getString("createTime"); // May 14, 2015
					// 5:09:02 PM
					// //日期Date转字符串String
					// DateFormat format = new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// Date createTimeDate = null;
					// try {
					// createTimeDate = (Date) format.parse(createTime);
					// } catch (ParseException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// String createTimeStr = format.format(createTimeDate); //
					// cmap.put("createTime", createTimeStr);

					String createTime = ((JSONObject) array.get(j)).getString("createTime"); // May
																								// 28,
																								// 2015
																								// 3:31:41
																								// PM
					// 字符串String转日期Date
					SimpleDateFormat df1 = new SimpleDateFormat("MMM dd, yyyy KK:mm:ss aaa", Locale.US);
					// Date createTimeDate = null;
					try {
						createTimeDate = df1.parse(createTime); // Thu May 28
																// 00:31:41
																// GMT+08:00
																// 2015
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 日期Date转字符串String
					SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String createTimeStr = df2.format(createTimeDate); // 2015-05-28
																		// 15:31:41

					cmap.put("createTime", createTimeStr);
					// cmap.put("createTime", ((JSONObject)
					// array.get(j)).getString("createTime"));
					cmap.put("remark", ((JSONObject) array.get(j)).getString("remark"));
					cmap.put("name", ((JSONObject) array.get(j)).getString("name"));
					cmap.put("photo", ((JSONObject) array.get(j)).getString("photo"));

					list.add(cmap);
				}

			}
		}
		return list;
	}

	/**
	 * 114 评论视频
	 */
	public static Map<String, Object> saveMediaCommentToJson(MediaComment mMediaComment) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mMediaComment);
		try {
			map = saveMediaComment(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> saveMediaComment(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.saveMediaComment, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				result = mJsonObject.getString("result");
				if (result.equals("0")) {
					map.put("result", result);
					// // 将Json字符串转换成--UserInfo对象
					// if (mJsonObject.getString("resultInfo") != null &&
					// mJsonObject.getString("resultInfo") != "" &&
					// mJsonObject.getString("resultInfo") != "null") {
					// VUserFansMediaMusicCount mVUserFansMediaMusicCount =
					// JsonUtil.convertToObject(VUserFansMediaMusicCount.class,
					// mJsonObject.getString("resultInfo"));
					//
					// // 将用户信息userInfo 存储在配置文件中
					// map.put("downloadMediaCount",
					// mVUserFansMediaMusicCount.getDownloadMediaCount());
					// map.put("mediaInfoCount",
					// mVUserFansMediaMusicCount.getMediaInfoCount());
					// map.put("userFansCount",
					// mVUserFansMediaMusicCount.getUserFansCount());
					// map.put("createUserFansCount",
					// mVUserFansMediaMusicCount.getCreateUserFansCount());
					// map.put("downloadMusicCount",
					// mVUserFansMediaMusicCount.getDownloadMusicCount());//用户ID
					// map.put("id", mVUserFansMediaMusicCount.getId());
					//
					// map.put("noValue", 0);
					// } else {
					//
					// map.put("noValue", 1);
					// }

				} else { // result为1或2
					map.put("result", result);
				}
			}

		} else {
			map.put("result", -1);
		}
		return map;
	}

	/****
	 * 115、保存用户意见反馈 menberFeedBackToJson
	 */
	public static Map<String, Object> menberFeedBackToJson(MemberFeedback memberFeedBack) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(memberFeedBack);
		try {
			map = saveMenberFeedBack(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, Object> saveMenberFeedBack(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.saveMemberFeedback, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				map.put("result", result);

			} else {
				map.put("result", result);
			}
		} else {
			map.put("result", -1);
		}
		return map;
	}

	// /**
	// * 105 根据用户id，查询用户详情
	// */
	// public static Map<String, Object> getNearManDetailToJson(
	// User mUser) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// JSONObject mJSONObject = userInfoToJson(mUser);
	// try {
	// map = getUserInfoById ( mJSONObject);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return map;
	// }
	//
	// public static Map<String, Object> getUserInfoById (
	// JSONObject mJSONObject) throws JSONException {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String jsonData =
	// SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson,
	// ConstantsUtil.getUserInfoById , mJSONObject);
	// String result;
	// if (jsonData != null) {
	// JSONObject mJsonObject = new JSONObject(jsonData);
	// result = mJsonObject.getString("result");
	// if (result.equals("0")) {
	//
	// }
	//
	// }
	// return map;
	// }

	/**
	 * 
	 * @Description:【301 首页 视频榜单】
	 * @param context
	 * @param vo
	 * @param @return
	 * @return ArrayList<Map<String,Object>>
	 */
	public static ArrayList<Map<String, Object>> mediaInfoPageVoListToJson(MediaInfoPageVo vo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(vo);
		try {
			list = getMediaInfoList(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Description:【301 首页 视频榜单】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return ArrayList<Map<String,Object>>
	 */
	public static ArrayList<Map<String, Object>> getMediaInfoList(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getMediaInfoList, mJSONObject);
		String result;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();

				pageMap.put("pageNo", mJsonObjectResult.getString("pageNo"));
				pageMap.put("pageSize", mJsonObjectResult.getString("pageSize"));
				pageMap.put("orderBy", mJsonObjectResult.getString("orderBy"));
				pageMap.put("order", mJsonObjectResult.getString("order"));
				pageMap.put("autoCount", mJsonObjectResult.getString("autoCount"));
				pageMap.put("totalCount", mJsonObjectResult.getString("totalCount"));
				pageMap.put("totalPage", mJsonObjectResult.getString("totalPage"));
				pageMap.put("first", mJsonObjectResult.getString("first"));
				pageMap.put("last", mJsonObjectResult.getString("last"));
				pageMap.put("prev", mJsonObjectResult.getString("prev"));
				pageMap.put("next", mJsonObjectResult.getString("next"));
				pageMap.put("firstPage", mJsonObjectResult.getString("firstPage"));
				pageMap.put("lastPage", mJsonObjectResult.getString("lastPage"));
				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> dMap = new HashMap<String, Object>();
					dMap.put("name", ((JSONObject) array.get(j)).getString("name"));
					dMap.put("praiseSum", ((JSONObject) array.get(j)).getInt("praiseSum"));
					dMap.put("downloadMediaSum", ((JSONObject) array.get(j)).getInt("downloadMediaSum"));
					dMap.put("userFansCount", ((JSONObject) array.get(j)).getInt("userFansCount"));
					dMap.put("address", ((JSONObject) array.get(j)).getString("address"));
					dMap.put("signature", ((JSONObject) array.get(j)).getString("signature"));
					dMap.put("photo", ((JSONObject) array.get(j)).getString("photo"));
					dMap.put("mediaOldName", ((JSONObject) array.get(j)).getString("mediaOldName"));
					dMap.put("mediaNewName", ((JSONObject) array.get(j)).getString("mediaNewName"));
					dMap.put("snapshot", ((JSONObject) array.get(j)).getString("snapshot"));
					dMap.put("remark", ((JSONObject) array.get(j)).getString("remark"));
					dMap.put("flag", ((JSONObject) array.get(j)).getInt("flag"));
					dMap.put("playCount", ((JSONObject) array.get(j)).getInt("playCount"));
					dMap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));
					dMap.put("danceMusicId", ((JSONObject) array.get(j)).getString("danceMusicId"));
					dMap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					dMap.put("id", ((JSONObject) array.get(j)).getInt("id"));

					dMap.put("top", j + 1);

					list.add(dMap);
				}

			}

		} else {
			resultMap.put("result", -1);
		}
		return list;
	}

	/**
	 * 
	 * @Description:【304 已录制视频】
	 * @param context
	 * @param vo
	 * @param @return
	 * @return ArrayList<Map<String,Object>>
	 */
	public static ArrayList<Map<String, Object>> mediaInfoPageListToJson(MediaInfoPageVo vo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(vo);
		try {
			list = getRecordedVideo(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Description:【304 已录制视频】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return ArrayList<Map<String,Object>>
	 */
	public static ArrayList<Map<String, Object>> getRecordedVideo(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getMediaInfoPageByCreateUser, mJSONObject);
		String result;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();

				pageMap.put("pageNo", mJsonObjectResult.getString("pageNo"));
				pageMap.put("pageSize", mJsonObjectResult.getString("pageSize"));
				pageMap.put("orderBy", mJsonObjectResult.getString("orderBy"));
				pageMap.put("order", mJsonObjectResult.getString("order"));
				pageMap.put("autoCount", mJsonObjectResult.getString("autoCount"));
				pageMap.put("totalCount", mJsonObjectResult.getString("totalCount"));
				pageMap.put("totalPage", mJsonObjectResult.getString("totalPage"));
				pageMap.put("first", mJsonObjectResult.getString("first"));
				pageMap.put("last", mJsonObjectResult.getString("last"));
				pageMap.put("prev", mJsonObjectResult.getString("prev"));
				pageMap.put("next", mJsonObjectResult.getString("next"));
				pageMap.put("firstPage", mJsonObjectResult.getString("firstPage"));
				pageMap.put("lastPage", mJsonObjectResult.getString("lastPage"));
				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> dMap = new HashMap<String, Object>();
					dMap.put("name", ((JSONObject) array.get(j)).getString("name"));
					dMap.put("praiseSum", ((JSONObject) array.get(j)).getInt("praiseSum"));
					dMap.put("downloadMediaSum", ((JSONObject) array.get(j)).getInt("downloadMediaSum"));
					dMap.put("userFansCount", ((JSONObject) array.get(j)).getInt("userFansCount"));
					dMap.put("address", ((JSONObject) array.get(j)).getString("address"));
					dMap.put("signature", ((JSONObject) array.get(j)).getString("signature"));
					dMap.put("photo", ((JSONObject) array.get(j)).getString("photo"));
					dMap.put("mediaOldName", ((JSONObject) array.get(j)).getString("mediaOldName"));
					dMap.put("mediaNewName", ((JSONObject) array.get(j)).getString("mediaNewName"));
					dMap.put("snapshot", ((JSONObject) array.get(j)).getString("snapshot"));
					dMap.put("remark", ((JSONObject) array.get(j)).getString("remark"));
					dMap.put("flag", ((JSONObject) array.get(j)).getInt("flag"));
					dMap.put("playCount", ((JSONObject) array.get(j)).getInt("playCount"));
					dMap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));
					dMap.put("danceMusicId", ((JSONObject) array.get(j)).getString("danceMusicId"));
					dMap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					dMap.put("id", ((JSONObject) array.get(j)).getInt("id"));

					list.add(dMap);
				}

			}

		} else {
			resultMap.put("result", -1);
		}
		return list;
	}

	/**
	 * 
	 * @Description:【上传视频】
	 * @param context
	 * @param mediaInfo
	 * @param @return
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> saveMediaInfoToJson(MediaInfo mediaInfo, int requestCode) {
		Map<String, Object> list = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(mediaInfo);
		try {
			list = saveMediaInfo(mJSONObject, requestCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Description:【上传视频】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> saveMediaInfo(JSONObject mJSONObject, int requestCode) throws JSONException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String methodName;
		if (requestCode == 0) {// 0:保存视频记录
			methodName = ConstantsUtil.saveMediaInfo;
		} else { // 1:调接口推送给关注我的人
			methodName = ConstantsUtil.pushUserFansListAfterSaveMedia;
		}
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, methodName, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			resultMap.put("result", result);

			if (result.equals("0")) {// 上传成功
				String resultInfo = mJsonObject.getString("resultInfo");
				resultMap.put("resultInfo", resultInfo);
			}

			if (result.equals("2")) {// 调接口推送给关注我的人
				String resultInfo = mJsonObject.getString("resultInfo");
				resultMap.put("resultInfo", resultInfo);
			}
		} else {
			resultMap.put("result", -1);
		}
		return resultMap;
	}

	/**
	 * 
	 * @Description:【获取上传凭证token】
	 * @param context
	 * @param keys
	 * @param @return
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> getKeysToJson(Keys keys) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject mJSONObject = userInfoToJson(keys);
		try {
			map = getKeys(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @Description:【获取上传凭证token】
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> getKeys(JSONObject mJSONObject) throws JSONException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getMark, mJSONObject);
		String result;
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			resultMap.put("result", result);

			if (result.equals("0")) {// 上传成功
				String resultInfo = mJsonObject.getString("resultInfo");
				resultMap.put("resultInfo", resultInfo);
			}
		} else {
			resultMap.put("result", -1);
		}
		return resultMap;
	}

	/**
	 * 
	 * @Description:【已下载舞曲】
	 * @param context
	 * @param vo
	 * @param @return
	 * @return ArrayList<Map<String,Object>>
	 */
	public static ArrayList<Map<String, Object>> downloadMusicPageListToJson(DownloadMusicPageVo vo) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject mJSONObject = userInfoToJson(vo);
		try {
			list = getDownloadMusicPagesByCreateUser(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Description:【已下载舞曲】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return ArrayList<Map<String,Object>>
	 */
	public static ArrayList<Map<String, Object>> getDownloadMusicPagesByCreateUser(JSONObject mJSONObject) throws JSONException {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String jsonData = SoapWebServiceUtil.getStringResponseData(ConstantsUtil.userJson, ConstantsUtil.getDownloadMusicPagesByCreateUser, mJSONObject);
		String result;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");
			if (result.equals("0")) {
				String resultInfo = mJsonObject.getString("resultInfo");
				JSONObject mJsonObjectResult = new JSONObject(resultInfo);

				Map<String, Object> pageMap = new HashMap<String, Object>();

				pageMap.put("pageNo", mJsonObjectResult.getString("pageNo"));
				pageMap.put("pageSize", mJsonObjectResult.getString("pageSize"));
				pageMap.put("orderBy", mJsonObjectResult.getString("orderBy"));
				pageMap.put("order", mJsonObjectResult.getString("order"));
				pageMap.put("autoCount", mJsonObjectResult.getString("autoCount"));
				pageMap.put("totalCount", mJsonObjectResult.getString("totalCount"));
				pageMap.put("totalPage", mJsonObjectResult.getString("totalPage"));
				pageMap.put("first", mJsonObjectResult.getString("first"));
				pageMap.put("last", mJsonObjectResult.getString("last"));
				pageMap.put("prev", mJsonObjectResult.getString("prev"));
				pageMap.put("next", mJsonObjectResult.getString("next"));
				pageMap.put("firstPage", mJsonObjectResult.getString("firstPage"));
				pageMap.put("lastPage", mJsonObjectResult.getString("lastPage"));
				list.add(0, pageMap);

				String data = mJsonObjectResult.getString("data");
				JSONArray array = new JSONArray(data);
				for (int j = 0; j < array.length(); j++) {
					Map<String, Object> dMap = new HashMap<String, Object>();

					dMap.put("musicId", ((JSONObject) array.get(j)).getInt("musicId"));
					dMap.put("name", ((JSONObject) array.get(j)).getString("name"));
					dMap.put("author", ((JSONObject) array.get(j)).getString("author"));
					dMap.put("fileNewName", ((JSONObject) array.get(j)).getString("fileNewName"));
					dMap.put("downloadCount", ((JSONObject) array.get(j)).getString("downloadCount"));
					dMap.put("id", ((JSONObject) array.get(j)).getString("id"));
					dMap.put("createUser", ((JSONObject) array.get(j)).getString("createUser"));
					dMap.put("createTime", ((JSONObject) array.get(j)).getString("createTime"));
					dMap.put("token", ((JSONObject) array.get(j)).getString("token"));
					dMap.put("fileSize", ((JSONObject) array.get(j)).getString("fileSize"));
					dMap.put("trackLength", ((JSONObject) array.get(j)).getString("trackLength"));
					list.add(dMap);
				}

			}

		} else {
			resultMap.put("result", -1);
		}
		return list;
	}

	/**
	 * 
	 * @Description:【【获取明星老师列表】
	 * @param context
	 * @param vo
	 * @param @return
	 * @return ArrayList<Map<String,Object>>
	 */
	public static List<TeacherDancerMusic> dancerListToJson(DownloadMusic dancerList) {
		List<TeacherDancerMusic> list = new ArrayList<TeacherDancerMusic>();
		JSONObject mJSONObject = userInfoToJson(dancerList);
		try {
			list = getVDanceMusicDancerList(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Description:【获取明星老师列表】接口
	 * @param context
	 * @param mJSONObject
	 * @param @return
	 * @param @throws JSONException
	 * @return ArrayList<Map<String,Object>>
	 */
	public static List<TeacherDancerMusic> getVDanceMusicDancerList(JSONObject mJSONObject) throws JSONException {
		List<TeacherDancerMusic> teacherList = new ArrayList<TeacherDancerMusic>();
		String jsonData = SoapWebServiceUtil.getStringResponseData("", ConstantsUtil.getVDanceMusicDancerList, mJSONObject);
		String result;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (jsonData != null) {
			JSONObject mJsonObject = new JSONObject(jsonData);
			result = mJsonObject.getString("result");

			String data = mJsonObject.getString("resultInfo");
			JSONArray array = new JSONArray(data);

//			for (int i = 0; i < array.length(); i++) {
//				TeacherDancerMusic teacherDancerMusic = new TeacherDancerMusic();
//				teacherDancerMusic.setName(((JSONObject) array.get(i)).getString("name"));
//				teacherDancerMusic.setAuthor(((JSONObject) array.get(i)).getString("author"));
//				teacherDancerMusic.setFileNewName(((JSONObject) array.get(i)).getString("fileNewName"));
//				teacherDancerMusic.setUserName(((JSONObject) array.get(i)).getString("userName"));
//				teacherDancerMusic.setDownloadCount(((JSONObject) array.get(i)).getString("downloadCount"));
//				teacherDancerMusic.setFileSize(((JSONObject) array.get(i)).getString("fileSize"));
//				teacherDancerMusic.setDancer(((JSONObject) array.get(i)).getString("dancer"));
//				teacherDancerMusic.setTrackLength(((JSONObject) array.get(i)).getString("trackLength"));
//				teacherDancerMusic.setMid(((JSONObject) array.get(i)).getString("id"));
//				teacherDancerMusic.setCreateUser(((JSONObject) array.get(i)).getString("createUser"));
//				teacherDancerMusic.setCreateTime(((JSONObject) array.get(i)).getString("createTime"));
//				teacherDancerMusic.setDownloadState(ContentValue.DOWNLOAD_STATE_SUSPEND);
//				teacherList.add(teacherDancerMusic);
//			}

		} else {
			resultMap.put("result", -1);
		}
		return teacherList;
	}

	public static Map<String, Object> mediaInfoPageVoToJson(Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultJsonData = HttpsInterfaces.httpPost(ConstantsUtil.HttpUrl, map);
		if (resultJsonData != null) {
			String result;
			try {
				JSONObject mJsonObject = new JSONObject(resultJsonData);
				result = mJsonObject.getString("result");
				map.put("result", result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			resultMap.put("result", -1);
		}
		return resultMap;

	}

	public static Map<String, Object> uploadFile(Context context, String filePath) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpsInterfaces.httpUpload(context, filePath);
		return resultMap;

	}

}
