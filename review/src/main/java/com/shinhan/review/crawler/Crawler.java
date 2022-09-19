package com.shinhan.review.crawler;

import org.json.simple.JSONObject;

import java.util.List;

public interface Crawler {

    List<JSONObject> getReview(String packageName);
}
