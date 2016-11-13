package com.android.app.showdance.model.glmodel;

import java.io.Serializable;
import java.util.List;

public class AdPicAndIconInfo {

    public static class Request {
        public Request() {
        }
    }

    public static class Response {
        private List<Ad> topPic; // 对应的主题信息
        private List<Category> icon; // 对应的主题图标
        private String theme; // 对应的主题名
        private int themeType; // 对应的主题类型
        
        public List<Ad> getTopPic() {
            return topPic;
        }

        public void setTopPic(List<Ad> topPic) {
            this.topPic = topPic;
        }

        public List<Category> getIcon() {
            return icon;
        }

        public void setIcon(List<Category> icon) {
            this.icon = icon;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public int getThemeType() {
            return themeType;
        }

        public void setThemeType(int themeType) {
            this.themeType = themeType;
        }

        @Override
        public String toString() {
            return "Response [topPic=" + topPic + ", icon=" + icon + ", theme=" + theme + ", themeType=" + themeType
                    + "]";
        }

    }

    /**
     * 功能：顶部广告图片部分
     * 
     * @author djd
     */
    public static class Ad {
        private String name; // 图片名称
        private int type; // 图片类型
        private String pic; // 网络图片地址
        private String url; // 图片对应的网页地址

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Ad [name=" + name + ", type=" + type + ", url=" + url + "]";
        }

    }

    /**
     * 功能：“首页”的分类导航部分
     * 
     * @author djd
     *
     */
    public static class Category implements Serializable{

        private static final long serialVersionUID = -631780173471269474L;
        
        private String name; // 分类图标名称
        private int type; // 分类图标类型

        public Category(String name, int type) {
            super();
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Category [name=" + name + ", type=" + type + "]";
        }

    }
}
