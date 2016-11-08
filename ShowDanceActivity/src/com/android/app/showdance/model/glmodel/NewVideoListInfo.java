package com.android.app.showdance.model.glmodel;

import java.io.Serializable;
import java.util.List;

public class NewVideoListInfo {

    public static class Request {

        private int type; // 传入0表示中国，1省，2市
        private String content; // 传入区域，如湖北省或者武汉市
        private String theme; // 主题名称
        private int pageNumber; // 请求的每页视频数量

        public Request() {
            type = 0; // 默认查询全国最热视频
        }

        public Request(int type, String content, String theme, int pageNumber) {
            this.type = type;
            this.content = content;
            this.theme = theme;
            this.pageNumber = pageNumber;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        @Override
        public String toString() {
            return "Request [type=" + type + ", content=" + content + ", theme=" + theme + ", pageNumber=" + pageNumber
                    + "]";
        }

    }

    public static class Response {

        private boolean flag;
        private String message;
        private receivedDate data;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public receivedDate getData() {
            return data;
        }

        public void setData(receivedDate data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Response [flag=" + flag + ", message=" + message + ", data=" + data + "]";
        }

    }

    public static class receivedDate {

        private int total;
        private String per_page;
        private int current_page;
        private int last_page;
        private String next_page_url;
        private String prev_page_url;
        private int from;
        private int to;
        private List<HotVideo> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getPer_page() {
            return per_page;
        }

        public void setPer_page(String per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public String getNext_page_url() {
            return next_page_url;
        }

        public void setNext_page_url(String next_page_url) {
            this.next_page_url = next_page_url;
        }

        public String getPrev_page_url() {
            return prev_page_url;
        }

        public void setPrev_page_url(String prev_page_url) {
            this.prev_page_url = prev_page_url;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public List<HotVideo> getData() {
            return data;
        }

        public void setData(List<HotVideo> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "receivedDate [total=" + total + ", per_page=" + per_page + ", current_page=" + current_page
                    + ", last_page=" + last_page + ", next_page_url=" + next_page_url + ", prev_page_url="
                    + prev_page_url + ", from=" + from + ", to=" + to + ", data=" + data + "]";
        }

    }

    public static class HotVideo implements Serializable {

        private static final long serialVersionUID = -318998164518552359L;

        private int id;
        private String user_id;
        private String name;
        private String img; // 视频截图URL地址
        private String created_at;
        private String updated_at;
        private String videoname;
        private String path;
        private int count; // 播放次数
        private int share_count; // 分享次数
        private String province;
        private String city;
        private String x;
        private String y;
        private String theme; // 主题名称
        private int hot; // 热度
        protected int status; // 视频当前审核状态
        protected String handle_user; // 视频操作者名称
        protected int handle_user_id; // 视频操作者ID
        protected String handle_created_at; // 视频操作者操作时间

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getVideoname() {
            return videoname;
        }

        public void setVideoname(String videoname) {
            this.videoname = videoname;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getShare_count() {
            return share_count;
        }

        public void setShare_count(int share_count) {
            this.share_count = share_count;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getHandle_user() {
            return handle_user;
        }

        public void setHandle_user(String handle_user) {
            this.handle_user = handle_user;
        }

        public int getHandle_user_id() {
            return handle_user_id;
        }

        public void setHandle_user_id(int handle_user_id) {
            this.handle_user_id = handle_user_id;
        }

        public String getHandle_created_at() {
            return handle_created_at;
        }

        public void setHandle_created_at(String handle_created_at) {
            this.handle_created_at = handle_created_at;
        }

        @Override
        public String toString() {
            return "HotVideo [id=" + id + ", user_id=" + user_id + ", name=" + name + ", img=" + img + ", created_at="
                    + created_at + ", updated_at=" + updated_at + ", videoname=" + videoname + ", path=" + path
                    + ", count=" + count + ", share_count=" + share_count + ", province=" + province + ", city=" + city
                    + ", x=" + x + ", y=" + y + ", theme=" + theme + ", hot=" + hot + ", status=" + status
                    + ", handle_user=" + handle_user + ", handle_user_id=" + handle_user_id + ", handle_created_at="
                    + handle_created_at + "]";
        }
    }
}
