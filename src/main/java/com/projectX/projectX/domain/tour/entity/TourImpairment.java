package com.projectX.projectX.domain.tour.entity;

import com.projectX.projectX.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourImpairment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("투어 편의시설 식별자")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Comment("휠체어")
    private Integer wheelChair;

    @Comment("점자블록")
    private Integer braileBlock;

    @Comment("오디오 가이드")
    private Integer audioGuide;

    @Comment("비디오 가이드 및 자막")
    private Integer videGuide;

    @Comment("유모차")
    private Integer stroller;

    @Comment("수유실")
    private Integer lactationroom;

    @Builder
    public TourImpairment(Long id, Tour tour, Integer wheelChair,
        Integer braileBlock, Integer audioGuide, Integer videGuide, Integer stroller,
        Integer lactationroom) {
        this.id = id;
        this.tour = tour;
        this.wheelChair = wheelChair;
        this.braileBlock = braileBlock;
        this.audioGuide = audioGuide;
        this.videGuide = videGuide;
        this.stroller = stroller;
        this.lactationroom = lactationroom;
    }
}
