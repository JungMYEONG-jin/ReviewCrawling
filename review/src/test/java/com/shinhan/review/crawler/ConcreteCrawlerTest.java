package com.shinhan.review.crawler;

import com.shinhan.review.crawler.apple.AppleAppId;
import com.shinhan.review.crawler.google.GoogleAppId;
import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ConcreteCrawlerTest {

    @Autowired
    ConcreteCrawler crawler;
    @Autowired
    ReviewRepository reviewRepository;

    @Description("android")
    @Test
    void andTest() {
        List<JSONObject> reviewList = crawler.getReviewList(GoogleAppId.sbank.getAppPkg(), OS.AND.getNumber());
        List<Review> reviews = new ArrayList<>();
        if (!reviewList.isEmpty()) {
            for (JSONObject jsonObject : reviewList) {
                ReviewDto reviewDto = new ReviewDto(jsonObject);
                reviewDto.setAppPkg(GoogleAppId.sbank.getAppPkg());
                reviewDto.setOsType(OS.AND.getNumber());
                reviews.add(reviewDto.toEntity());
            }
        }
        reviewRepository.saveAll(reviews);
    }

    @Description("ios")
    @Test
    void appleTest() {
        List<JSONObject> reviewList = crawler.getReviewList(AppleAppId.O2O.getAppPkg(), OS.IOS.getNumber());
        List<Review> reviews = new ArrayList<>();
        if (!reviewList.isEmpty()) {
            for (JSONObject jsonObject : reviewList) {
                ReviewDto reviewDto = new ReviewDto(jsonObject);
                reviewDto.setAppPkg(AppleAppId.O2O.toString());
                reviewDto.setOsType(OS.IOS.getNumber());
                reviews.add(reviewDto.toEntity());
            }
        }
        reviewRepository.saveAll(reviews);
    }

    @Description("존재하지않는 타입")
    @Test
    void failTest() {
        Assertions.assertThatThrownBy(()->{crawler.getReviewList(AppleAppId.salimi.getAppPkg(),"3");}).isInstanceOf(IllegalArgumentException.class);
    }
}