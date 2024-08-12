package com.projectX.projectX.domain.review.entity;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.member.entity.Member;
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
public class CafeReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_review_id")
    private Long id;

    @Comment("리뷰 내용")
    @Column(length = 500, nullable = false)
    private String contents;

    @Comment("리뷰 별점")
    @Column(nullable = false)
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("유저 id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    @Comment("카페 id")
    private Cafe cafe;

    @OneToMany(mappedBy = "cafeReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeReviewImage> cafeReviewImages;

    @Builder
    public CafeReview(String contents, Integer score, Member user, Cafe cafe) {
        this.contents = contents;
        this.score = score;
        this.user = user;
        this.cafe = cafe;
        this.cafeReviewImages = new ArrayList<>();
    }
}
