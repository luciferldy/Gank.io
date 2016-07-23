package com.gank.io.model;

import java.util.Date;

/**
 * Created by lucifer on 16-1-5.
 */
public class ContentItem {

    public static final String MEI_ZHI = "福利";
    public static final String ANDROID = "Android";

    public static final String OBJECT_ID = "_id";
    public static final String CREATE_AT = "createdAt";
    public static final String DESC = "desc";
    public static final String PUBLISHED_AT = "publishedAt";
    public static final String SOURCE = "source";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String USED = "used";
    public static final String WHO = "who";

    private String who;
    private Date publishedAt;
    private String desc;
    private String type;
    private String url;
    private String source;
    private boolean used;
    private String objectId;
    private Date createdAt;
    private boolean isCategory = false;

    public void setWho(String who) {
        this.who = who;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsCategory(boolean isCategory) {
        this.isCategory = isCategory;
    }

    public String getWho() {
        return who;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public boolean isUsed() {
        return used;
    }

    public String getObjectId() {
        return objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getSource() {
        return source;
    }

    public boolean isCategory() {
        return isCategory;
    }

    @Override
    public String toString() {
        return OBJECT_ID + "=" + objectId + "\n"
                + CREATE_AT + "=" + createdAt + "\n"
                + DESC + desc + "=" + "\n"
                + PUBLISHED_AT + "=" + publishedAt + "\n"
                + SOURCE + "=" + source + "\n"
                + TYPE + "=" + type + "\n"
                + URL + "=" + url + "\n"
                + USED + "=" + used + "\n"
                + WHO + "=" + who;
    }
}
