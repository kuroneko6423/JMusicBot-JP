package dev.cosgy.JMusicBot.cache;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CacheObject {

    @SerializedName("Cache")
    @Expose
    private List<Cache> cache = null;

    public List<Cache> getCache() {
        return cache;
    }

    public void setCache(List<Cache> cache) {
        this.cache = cache;
    }

}
