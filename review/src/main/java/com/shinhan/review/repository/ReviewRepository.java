package com.shinhan.review.repository;

import com.shinhan.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAppPkg(String appPkg);
    List<Review> findByOsType(String osType);

    // 전체 반환
    Page<Review> findAll(Pageable pageable);

    // 조건 1
    // OS로 검색
    Page<Review> findByOsType(Pageable pageable, String osType);
    // 시작일 검색
    Page<Review> findByCreatedDateAfter(Pageable pageable, String createdDate);
    // 종료일 검색
    Page<Review> findByCreatedDateBefore(Pageable pageable, String createdDate);
    // app 이름
    Page<Review> findByAppPkg(Pageable pageable, String appPkg);

    // 조건 2
    // 시작 OS
    Page<Review> findByCreatedDateAfterAndOsType(Pageable pageable, String createdDate, String osType);
    // 종료 OS
    Page<Review> findByCreatedDateBeforeAndOsType(Pageable pageable, String createdDate, String osType);
    // 시작 종료
    @Query("select r from Review r where r.createdDate between :start and :end")
    Page<Review> searchByDate(Pageable pageable, @Param("start") String start, @Param("end") String end); // 날짜 기반 검색
    // 시작 app
    Page<Review> findByCreatedDateAfterAndAppPkg(Pageable pageable, String createdDate, String appPkg);
    // 종료 app
    Page<Review> findByCreatedDateBeforeAndAppPkg(Pageable pageable, String createdDate, String appPkg);
    // OS app
    Page<Review> findByOsTypeAndAppPkg(Pageable pageable, String osType, String appPkg);

    // 조건 3
    // 시작 종료 OS
    @Query("select r from Review r where r.createdDate between :start and :end and r.osType = :type")
    Page<Review> searchByDateAndOsType(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("type") String type);
    // 시작 종료 app
    @Query("select r from Review r where r.createdDate between :start and :end and r.appPkg = :appPkg")
    Page<Review> searchByDateAndAppPkg(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("appPkg") String appPkg);
    // 시작 os app
    Page<Review> findByCreatedDateAfterAndOsTypeAndAppPkg(Pageable pageable, String createdDate, String osType, String appPkg);
    // 종료 os app
    Page<Review> findByCreatedDateBeforeAndOsTypeAndAppPkg(Pageable pageable, String createdDate, String osType, String appPkg);

    // 조건 4
    // 시작 종료 os app
    @Query("select r from Review r where r.createdDate between :start and :end and r.osType = :type and r.appPkg = :appPkg")
    Page<Review> searchByDateAAndOsTypeAndAppPkg(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("type") String type, @Param("appPkg") String appPkg);


}

