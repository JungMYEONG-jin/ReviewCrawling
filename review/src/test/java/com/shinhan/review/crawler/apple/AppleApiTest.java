package com.shinhan.review.crawler.apple;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppleApiTest {

    AppleApi controller = new AppleApi();

    @Test
    void review() throws MalformedURLException, NoSuchAlgorithmException {
        List<JSONObject> allReviews = controller.getAllReviews(AppleAppId.O2O.getAppPkg());
        for (JSONObject allReview : allReviews) {
            System.out.println("allReview = " + allReview);
        }
    }
}