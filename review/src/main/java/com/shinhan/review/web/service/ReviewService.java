package com.shinhan.review.web.service;

import com.shinhan.review.crawler.ConcreteCrawler;
import com.shinhan.review.crawler.OS;
import com.shinhan.review.crawler.apple.AppleAppId;
import com.shinhan.review.crawler.google.GoogleAppId;
import com.shinhan.review.entity.Review;
import com.shinhan.review.entity.dto.ReviewDto;
import com.shinhan.review.repository.ReviewRepository;
import com.shinhan.review.search.form.SearchForm;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ConcreteCrawler crawler;
    @Autowired
    ReviewRepository reviewRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void saveAll(List<Review> reviews){
        reviewRepository.saveAll(reviews);
    }

    public void saveReviews(String appName, String osType){
        String packageName = getPackageName(appName, osType);
        List<JSONObject> reviewList = crawler.getReviewList(packageName, osType);
        List<Review> reviews = new ArrayList<>();
        if (!reviewList.isEmpty()) {
            for (JSONObject jsonObject : reviewList) {
                ReviewDto reviewDto = new ReviewDto(jsonObject);
                reviewDto.setAppPkg(appName);
                reviewDto.setOsType(osType);
                reviews.add(reviewDto.toEntity());
            }
        }
        reviewRepository.saveAll(reviews);
        logger.info("{} 크롤링 완료", packageName);
    }

    private String getPackageName(String packageName, String osType) {
        String pack = "";
        if (osType.equals("1")){
            GoogleAppId id = Arrays.stream(GoogleAppId.values()).filter(app -> app.name().equals(packageName)).findAny().orElse(null);
            pack = id.getAppPkg();
//            for(GoogleAppId google : GoogleAppId.values()){
//                if(google.name().equals(packageName))
//                {
//                    pack = google.getAppPkg();
//                    break;
//                }
//            }
        }else if(osType.equals("2")){
            AppleAppId id = Arrays.stream(AppleAppId.values()).filter(app -> app.name().equals(packageName)).findAny().orElse(null);
            pack = id.getAppPkg();
//            for(AppleAppId apple : AppleAppId.values()){
//                if(apple.name().equals(packageName))
//                {
//                    pack = apple.getAppPkg();
//                    break;
//                }
//            }
        }
        return pack;
    }

    public void saveOne(Review review){
        reviewRepository.save(review);
    }

    public Review findOne(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    public Page<Review> findAll(Pageable pageable){
        return reviewRepository.findAll(pageable);
    }

    public List<Review> findByAppPkg(String appPkg){
        return reviewRepository.findByAppPkg(appPkg);
    }

    public List<Review> findByType(String osType){
        return reviewRepository.findByOsType(osType);
    }
    
    private boolean isEmpty(String text){
        if(text==null || text.isEmpty())
            return true;
        return false;
    }

    public Page<Review> searchByCondition(Pageable pageable, SearchForm form){
        // 날짜 지정 안했을때
        if (form.getStart()==null && form.getEnd()==null)
        {
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return findAll(pageable);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByOsType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByOsTypeAndAppPkg(pageable, form);
        }

        // 시작일만 지정한경우
        if(form.getStart()!=null && form.getEnd()==null){
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfter(pageable, form);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfterAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfterAndOSType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateAfterAndOsTypeAndAppPkg(pageable, form);
        }

        // 종료일만 지정한 경우
        if(form.getStart()==null && form.getEnd()!=null){
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateBefore(pageable, form);
            else if (form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateBeforeAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByCreatedDateBeforeAndOSType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByCreatedDateBeforeAndOsTypeAndAppPkg(pageable, form);
        }

        // 시작, 종료 지정 했을때
        if (form.getStart()!=null && form.getEnd()!=null) {
            if (form.getOs()==null && isEmpty(form.getAppPkg()))
                return searchByDate(pageable, form);
            else if(form.getOs()==null && !isEmpty(form.getAppPkg()))
                return searchByDateAndAppPkg(pageable, form);
            else if(form.getOs()!=null && isEmpty(form.getAppPkg()))
                return searchByDateAndOsType(pageable, form);
            else if(form.getOs()!=null && !isEmpty(form.getAppPkg()))
                return searchByDateAndOsTypeAndAppPkg(pageable, form);
        }

        throw new IllegalStateException("검색 조건이 잘못되었습니다...");
    }


    public Page<Review> searchByOsType(Pageable pageable, SearchForm form){
        return reviewRepository.findByOsType(pageable, form.getOs().getNumber());
    }

    public Page<Review> searchByDate(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDate(pageable, start, end);
    }

    public Page<Review> searchByDateAndOsType(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAndOsType(pageable, start, end, form.getOs().getNumber());
    }

    public Page<Review> searchByCreatedDateAfter(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfter(pageable, start);
    }

    public Page<Review> searchByCreatedDateBefore(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBefore(pageable, end);
    }

    public Page<Review> searchByCreatedDateAfterAndOSType(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndOsType(pageable, start, form.getOs().getNumber());
    }

    public Page<Review> searchByCreatedDateBeforeAndOSType(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndOsType(pageable, end, form.getOs().getNumber());
    }

    // app
    public Page<Review> searchByAppPkg(Pageable pageable, SearchForm form){
        return reviewRepository.findByAppPkg(pageable,  form.getAppPkg());
    }
    // os app
    public Page<Review> searchByOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        return reviewRepository.findByOsTypeAndAppPkg(pageable, form.getOs().getNumber(), form.getAppPkg());
    }
    // start app
    public Page<Review> searchByCreatedDateBeforeAndAppPkg(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndAppPkg(pageable, end, form.getAppPkg());
    }
    // end app
    public Page<Review> searchByCreatedDateAfterAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndAppPkg(pageable, start, form.getAppPkg());
    }
    // start end app
    public Page<Review> searchByDateAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAndAppPkg(pageable, start, end, form.getAppPkg());
    }
    // start os app
    public Page<Review> searchByCreatedDateAfterAndOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        return reviewRepository.findByCreatedDateAfterAndOsTypeAndAppPkg(pageable, start, form.getOs().getNumber(), form.getAppPkg());
    }
    // end os app
    public Page<Review> searchByCreatedDateBeforeAndOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.findByCreatedDateBeforeAndOsTypeAndAppPkg(pageable, end, form.getOs().getNumber(), form.getAppPkg());
    }

    // start end os app
    public Page<Review> searchByDateAndOsTypeAndAppPkg(Pageable pageable, SearchForm form){
        String start = getFormattedDate(form.getStart().toString());
        String end = getFormattedDate(form.getEnd().toString());
        return reviewRepository.searchByDateAAndOsTypeAndAppPkg(pageable, start, end, form.getOs().getNumber(), form.getAppPkg());
    }



    private String getFormattedDate(String dates) {
        dates = dates.replaceAll("-","");
        dates = dates+"000000";
        return dates;
    }

    public void clear(){
        reviewRepository.deleteAll();
    }

}
