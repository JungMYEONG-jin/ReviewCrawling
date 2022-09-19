package com.shinhan.review.crawler;

import com.shinhan.review.crawler.apple.AppleApi;
import com.shinhan.review.crawler.google.GoogleApi;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteCrawler{

    /**
     * 1 and, 2 ios
     * @param pacakgeName
     * @param osType
     * @return
     */
    public List<JSONObject> getReviewList(String pacakgeName, String osType){
        Crawler crawler = null;
        if (osType.equals(OS.AND.getNumber()))
        {
            crawler = new GoogleApi();
        }else if (osType.equals(OS.IOS.getNumber())){
            crawler = new AppleApi();
        }else{
            throw new IllegalArgumentException("존재하지 않는 타입닙니다.");
        }
        return crawler.getReview(pacakgeName);
    }

}
