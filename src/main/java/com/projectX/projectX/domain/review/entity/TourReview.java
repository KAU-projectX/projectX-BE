package com.projectX.projectX.domain.review.entity;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.review.util.TourReviewMapper;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.global.common.BaseEntity;
import com.projectX.projectX.global.common.RecommendationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class TourReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_review_id")
    private Long id;

    @Comment("리뷰 내용")
    @Column(length = 500, nullable = false)
    private String contents;

    @Comment("리뷰 별점")
    @Column(nullable = false)
    private Integer score;

    @Comment("추천 여부")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationType recommendationType;

    @ManyToOne
    @Comment("유저 id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @Comment("투어 id")
    private Tour tour;

    @Comment("tour 리뷰 이미지")
    @OneToMany(mappedBy = "tourReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourReviewImage> tourReviewImages;

    @Builder
    public TourReview(String contents, Integer score, RecommendationType recommendationType,
        Member user, Tour tour) {
        this.contents = contents;
        this.score = score;
        this.recommendationType = recommendationType;
        this.user = user;
        this.tour = tour;
        this.tourReviewImages = new ArrayList<>();
    }

    public void createReviewImage(String url) {
        TourReviewImage tourReviewImage = TourReviewMapper.toTourReviewImage(this, url);
        this.tourReviewImages.add(tourReviewImage);
    }
}
