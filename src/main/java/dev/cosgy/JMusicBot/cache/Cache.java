
package dev.cosgy.JMusicBot.cache;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cache {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("isStream")
    @Expose
    private Boolean isStream;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("userId")
    @Expose
    private String userId;

    public Cache(String title, String author, long length, String identifier, boolean isStream, String uri, long userId) {
        this.title = title;
        this.author = author;
        this.length = String.valueOf(length);
        this.identifier = identifier;
        this.isStream = isStream;
        this.url = uri;
        this.userId = String.valueOf(userId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getIsStream() {
        return isStream;
    }

    public void setIsStream(Boolean isStream) {
        this.isStream = isStream;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() { return userId;}

    public void setUserId(String userId){ this.userId = userId; }

}
