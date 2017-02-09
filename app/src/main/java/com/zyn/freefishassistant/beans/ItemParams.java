package com.zyn.freefishassistant.beans;

import java.io.Serializable;
import java.util.Map;

/**
 * 文件名 ItemParams
 * 调用闲鱼的参数实体类
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/9
 * 版权声明 Created by ZengYinan
 */

public class ItemParams implements Serializable{
    private String fishPoolId;
    private String fishpondTopic;
    private boolean isManager;
    private boolean isSnapshot;
    private boolean isTop;
    private String itemId;
    private boolean scrollToComment;
    private boolean showKeyboard;
    private String sourceTrack;
    private Map trackParams;
    private String trackParamsJson;
    private String withAwards;

    public String getFishPoolId() {
        return fishPoolId;
    }

    public void setFishPoolId(String fishPoolId) {
        this.fishPoolId = fishPoolId;
    }

    public String getFishpondTopic() {
        return fishpondTopic;
    }

    public void setFishpondTopic(String fishpondTopic) {
        this.fishpondTopic = fishpondTopic;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean isSnapshot() {
        return isSnapshot;
    }

    public void setSnapshot(boolean snapshot) {
        isSnapshot = snapshot;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isScrollToComment() {
        return scrollToComment;
    }

    public void setScrollToComment(boolean scrollToComment) {
        this.scrollToComment = scrollToComment;
    }

    public boolean isShowKeyboard() {
        return showKeyboard;
    }

    public void setShowKeyboard(boolean showKeyboard) {
        this.showKeyboard = showKeyboard;
    }

    public String getSourceTrack() {
        return sourceTrack;
    }

    public void setSourceTrack(String sourceTrack) {
        this.sourceTrack = sourceTrack;
    }

    public Map getTrackParams() {
        return trackParams;
    }

    public void setTrackParams(Map trackParams) {
        this.trackParams = trackParams;
    }

    public String getTrackParamsJson() {
        return trackParamsJson;
    }

    public void setTrackParamsJson(String trackParamsJson) {
        this.trackParamsJson = trackParamsJson;
    }

    public String getWithAwards() {
        return withAwards;
    }

    public void setWithAwards(String withAwards) {
        this.withAwards = withAwards;
    }
}
