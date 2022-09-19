package com.shinhan.review.crawler.google;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleApiTest {

    GoogleApi controller = new GoogleApi();

    @Test
    void review() throws MalformedURLException {
        List<JSONObject> reviewList = controller.getReviewList(GoogleAppId.sbank.getAppPkg());
        for (JSONObject jsonObject : reviewList) {
            System.out.println("jsonObject = " + jsonObject.toJSONString());
        }
    }
}