package com.projectX.projectX.domain.review.entity;

import com.projectX.projectX.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_review_image_id")
    private Long id;

    @Comment("리뷰 이미지")
    private String image;

    @ManyToOne
    @Comment("투어 리뷰 id")
    private TourReview tourReview;

    @Builder
    public TourReviewImage(Long id, String image, TourReview tourReview) {
        this.id = id;
        this.image = image;
        this.tourReview = tourReview;
    }

}
