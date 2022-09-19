package com.shinhan.review.entity.dto;

import com.shinhan.review.entity.Review;
import org.json.simple.JSONObject;

public class ReviewDto {
    private Long id;
    private String appVersion;
    private String createdDate; // 리뷰 작성일
    private String nickname;
    private String rating;
    private String body; // 리뷰
    private String responseBody;
    private String answeredDate; // 답변일
    private String device;
    private String appPkg;
    private String osType;

    public Review toEntity(){
        return new Review(id, appVersion, createdDate, nickname, rating, body,responseBody, answeredDate, device, appPkg, osType);
    }

    public ReviewDto(JSONObject jsonObject){
        if (jsonObject!=null){
            if (jsonObject.containsKey("appVersion"))
                appVersion = jsonObject.get("appVersion").toString();
            if (jsonObject.containsKey("createdDate"))
                createdDate = jsonObject.get("createdDate").toString();
            if (jsonObject.containsKey("nickName"))
                nickname = jsonObject.get("nickName").toString();
            if (jsonObject.containsKey("rating"))
                rating = jsonObject.get("rating").toString();
            if (jsonObject.containsKey("body"))
                body = jsonObject.get("body").toString();
            if (jsonObject.containsKey("responseBody"))
                responseBody = jsonObject.get("responseBody").toString();
            if (jsonObject.containsKey("answeredDate"))
                answeredDate = jsonObject.get("answeredDate").toString();
            if (jsonObject.containsKey("device"))
                device = jsonObject.get("device").toString();

        }
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public void setAppPkg(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public void setAnsweredDate(String answeredDate) {
        this.answeredDate = answeredDate;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
