package com.shinhan.review.entity;

import javax.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appVersion;
    private String createdDate; // 리뷰 작성일
    private String nickname;
    private String rating;
    @Column(length = 4000)
    private String body; // 리뷰
    @Column(length = 4000)
    private String responseBody;
    private String answeredDate; // 답변일
    private String device;
    private String appPkg;
    private String osType;

    public Review() {
    }

    public Review(Long id, String appVersion, String createdDate, String nickname, String rating, String body, String responseBody, String answeredDate, String device, String appPkg, String osType) {
        this.id = id;
        this.appVersion = appVersion;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.rating = rating;
        this.body = body;
        this.responseBody = responseBody;
        this.answeredDate = answeredDate;
        this.device = device;
        this.appPkg = appPkg;
        this.osType = osType;
    }

    public Long getId() {
        return id;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public String getOsType() {
        return osType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRating() {
        return rating;
    }

    public String getBody() {
        return body;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public String getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "Review{" +
                "appVersion='" + appVersion + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", nickname='" + nickname + '\'' +
                ", rating='" + rating + '\'' +
                ", body='" + body + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", answeredDate='" + answeredDate + '\'' +
                ", device='" + device + '\'' +
                ", appPkg='" + appPkg + '\'' +
                ", osType='" + osType + '\'' +
                '}';
    }
}
