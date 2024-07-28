package com.projectX.projectX.domain.review.entity;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Float score;

    @ManyToOne
    @Comment("유저 id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @Comment("투어 id")
    private Tour tour;

    @Comment("tour 리뷰 이미지")
    @OneToMany(mappedBy = "TourReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourReviewImage> tourReviewImages;

    @Builder
    public TourReview(Long id, String contents, Float score, Member user, Tour tour) {
        this.id = id;
        this.contents = contents;
        this.score = score;
        this.user = user;
        this.tour = tour;
        this.tourReviewImages = new ArrayList<>();
    }
}
