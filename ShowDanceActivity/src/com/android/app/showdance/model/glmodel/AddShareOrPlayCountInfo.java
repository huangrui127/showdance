package com.android.app.showdance.model.glmodel;

public class AddShareOrPlayCountInfo {

    public static class Request {
        private int video_id;
        private int type; // type为1，表示需要增加播放次数；type为0，表示需要增加分享次数。
        
        public Request(int videoId,int type){
            this.video_id = videoId;
            this.type = type;
        }
        
        public Request(int videoId) {
            video_id = videoId;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

    public static class Response {

        protected boolean flag;
        protected String message;
        protected String data;

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

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Response [flag=" + flag + ", message=" + message + ", data=" + data + "]";
        }

    }

    // public static class Response {
    // private Video video;
    //
    // public Video getVideo() {
    // return video;
    // }
    //
    // public void setVideo(Video video) {
    // this.video = video;
    // }
    //
    // }

    // public static class Video {
    // private int id;
    // private String user_id;
    // private String name;
    // private String img;
    // private String created_at;
    // private String updated_at;
    // private String videoname;
    // private String path;
    // private int count;
    // private String share_content;
    // private int share_count;
    //
    // public int getId() {
    // return id;
    // }
    //
    // public void setId(int id) {
    // this.id = id;
    // }
    //
    // public String getUser_id() {
    // return user_id;
    // }
    //
    // public void setUser_id(String user_id) {
    // this.user_id = user_id;
    // }
    //
    // public String getName() {
    // return name;
    // }
    //
    // public void setName(String name) {
    // this.name = name;
    // }
    //
    // public String getImg() {
    // return img;
    // }
    //
    // public void setImg(String img) {
    // this.img = img;
    // }
    //
    // public String getCreated_at() {
    // return created_at;
    // }
    //
    // public void setCreated_at(String created_at) {
    // this.created_at = created_at;
    // }
    //
    // public String getUpdated_at() {
    // return updated_at;
    // }
    //
    // public void setUpdated_at(String updated_at) {
    // this.updated_at = updated_at;
    // }
    //
    // public String getVideoname() {
    // return videoname;
    // }
    //
    // public void setVideoname(String videoname) {
    // this.videoname = videoname;
    // }
    //
    // public String getPath() {
    // return path;
    // }
    //
    // public void setPath(String path) {
    // this.path = path;
    // }
    //
    // public int getCount() {
    // return count;
    // }
    //
    // public void setCount(int count) {
    // this.count = count;
    // }
    //
    // public String getShare_content() {
    // return share_content;
    // }
    //
    // public void setShare_content(String share_content) {
    // this.share_content = share_content;
    // }
    //
    // public int getShare_count() {
    // return share_count;
    // }
    //
    // public void setShare_count(int share_count) {
    // this.share_count = share_count;
    // }
    //
    // }

}
