package com.shinhan.review.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ReviewId implements Serializable {

    @Id
    private String createdDate;
    @Id
    private String nickname;

    public ReviewId() {
    }

    public ReviewId(String createdDate, String nickname) {
        this.createdDate = createdDate;
        this.nickname = nickname;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(createdDate, reviewId.createdDate) && Objects.equals(nickname, reviewId.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDate, nickname);
    }
}
