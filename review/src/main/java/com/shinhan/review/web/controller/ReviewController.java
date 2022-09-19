package com.shinhan.review.web.controller;

import com.shinhan.review.entity.Review;
import com.shinhan.review.search.form.CrawlingForm;
import com.shinhan.review.search.form.SearchForm;
import com.shinhan.review.web.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SearchForm form = new SearchForm();

    @GetMapping("/reviews/{appPkg}")
    public String getReviewByAppPkg(Model model, @PathVariable(value = "appPkg") String appPkg){
        List<Review> reviews = reviewService.findByAppPkg(appPkg);
        model.addAttribute("reviews", reviews);
        return "review/reviewByPkg";
    }

    @GetMapping("/")
    public String goHome(){
        logger.info("home controller");
        return "home";
    }

    @GetMapping("/crawling/{packageName}/{osType}")
    public String crawlingReviews(@PathVariable String packageName, @PathVariable String osType){
        logger.info("crawling 시작");
        reviewService.saveReviews(packageName, osType);
        return "redirect:/";
    }

    @GetMapping("/crawling/search")
    public String getCrawlingSearchForm(Model model){
        model.addAttribute("crawlingForm", new CrawlingForm());
        return "review/searchForm";
    }

    @PostMapping("/crawling/search")
    public String postCrawlingSearchForm(Model model, @ModelAttribute("crawlingForm") CrawlingForm crawlingForm){
        logger.info("크롤링을 시작합니다...");
        reviewService.saveReviews(crawlingForm.getAppId(), crawlingForm.getOs().getNumber());
        return "redirect:/";
    }


    @GetMapping("/reviews")
    public String getReviewList(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        Page<Review> reviews = reviewService.findAll(pageable);
        model.addAttribute("reviews", reviews);
        return "review/reviewList";
    }

    @GetMapping("/reviews/search")
    public String searchReviewListGet(Model model, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        Page<Review> reviews = reviewService.searchByCondition(pageable, form); //처음만 init 하면
        model.addAttribute("searchForm", form);
        model.addAttribute("reviews", reviews);
        return "review/searchPage";
    }

    @PostMapping("/reviews/search")
    public String searchReviewListPost(Model model, @ModelAttribute("searchForm") SearchForm dateSearch, @PageableDefault(page=0, size = 10, direction = Sort.Direction.DESC)Pageable pageable){
        this.form = dateSearch;
        Page<Review> reviews = reviewService.searchByCondition(pageable, form);
        model.addAttribute("reviews",reviews);
        return "review/searchPage";
    }

}
