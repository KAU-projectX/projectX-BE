package com.projectX.projectX.domain.tour.entity;

import com.projectX.projectX.global.common.BaseEntity;
import com.projectX.projectX.global.common.ContentType;
import com.projectX.projectX.global.common.JejuRegion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("투어 api 식별자")
    private Long id;

    @Column(length = 100)
    @Comment("주소")
    private String address;

    @Column(length = 50)
    @Comment("상세주소")
    private String spec_address;

    @Comment("우편번호")
    private Long zipCode;

    @Column(nullable = false)
    @Comment("콘텐츠 id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Comment("콘텐츠 타입 id")
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    @Comment("제주도 지역 구분")
    private JejuRegion jejuRegion;

    @Column(nullable = false)
    @Comment("대표 이미지 ")
    private String imageUrl;

    @Column(nullable = false)
    @Comment("GPS X좌표")
    private float mapX;

    @Column(nullable = false)
    @Comment("GPS Y좌표")
    private float mapY;

    @Column(length = 100, nullable = false)
    @Comment("콘텐츠 제목")
    private String title;

    @Column(length = 30)
    @Comment("전화번호")
    private String phone;

    @Comment("홈페이지 URL")
    private String homepageUrl;

    @Comment("관광지 설명")
    @Column(columnDefinition = "TEXT")
    private String overview;

    @OneToMany(mappedBy = "tour")
    private List<TourImage> tourImageList = new ArrayList<>();

    @Comment("카카오맵 URL")
    private String kakaoMapUrl;

    @Builder
    public Tour(Long id, String address, String spec_address, Long zipCode, Long contentId,
        ContentType contentType, JejuRegion jejuRegion, String imageUrl, float mapX, float mapY,
        String title, String phone, String homepageUrl, String overview, String kakaoMapUrl) {
        this.id = id;
        this.address = address;
        this.spec_address = spec_address;
        this.zipCode = zipCode;
        this.contentId = contentId;
        this.contentType = contentType;
        this.jejuRegion = jejuRegion;
        this.imageUrl = imageUrl;
        this.mapX = mapX;
        this.mapY = mapY;
        this.title = title;
        this.phone = phone;
        this.homepageUrl = homepageUrl;
        this.overview = overview;
        this.kakaoMapUrl = kakaoMapUrl;
    }

    public void updateHomePageAndOverview(String homepage, String overview) {
        this.homepageUrl = homepage;
        this.overview = overview;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateKakaoMapUrl(String kakaoMapUrl){
        this.kakaoMapUrl = kakaoMapUrl;
    }
}