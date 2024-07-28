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
public class CafeReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_review_image_id")
    private Long id;

    @Comment("리뷰 이미지")
    private String image;

    @ManyToOne
    @Comment("카페 리뷰 id")
    private CafeReview cafeReview;

    @Builder
    public CafeReviewImage(Long id, String image, CafeReview cafeReview) {
        this.id = id;
        this.image = image;
        this.cafeReview = cafeReview;
    }

}
