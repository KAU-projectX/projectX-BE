package com.projectX.projectX.domain.cafe.entity;

import com.projectX.projectX.global.common.BaseEntity;
import com.projectX.projectX.global.common.CafeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("카페 id")
    @Column(length = 100)
    private String cafeId;

    @Comment("카페 이름")
    @Column(length = 100, nullable = false)
    private String name;

    @Comment("카페 타입")
    @Enumerated(EnumType.STRING)
    private CafeType cafeType;

    @Comment("카페 주소")
    @Column(nullable = false)
    private String address;

    @Comment("위도")
    private double latitude;

    @Comment("경도")
    private double longitude;

    @Builder
    public Cafe(Long id, String name, CafeType cafeType, String address, double latitude,
        double longitude) {
        this.id = id;
        this.name = name;
        this.cafeType = cafeType;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
