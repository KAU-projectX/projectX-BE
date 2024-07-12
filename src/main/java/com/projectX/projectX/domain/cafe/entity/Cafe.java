package com.projectX.projectX.domain.cafe.entity;

import com.projectX.projectX.global.common.BaseEntity;
import com.projectX.projectX.global.common.CafeType;
import com.projectX.projectX.global.common.JejuRegion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
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

    @Comment("카페 URI")
    private String uri;

    @Enumerated(EnumType.STRING)
    @Comment("제주도 지역 구분")
    private JejuRegion jejuRegion;

    @Comment("전화번호")
    private String phoneNumber;

    @Comment("user 스크랩 정보")
    @OneToMany(mappedBy = "id.cafe", cascade = CascadeType.ALL)
    private List<Scrap> scraps;

    @Builder
    public Cafe(Long id, String name, CafeType cafeType, String address, double latitude,
        double longitude, String uri, JejuRegion jejuRegion, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.cafeType = cafeType;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uri = uri;
        this.jejuRegion = jejuRegion;
        this.phoneNumber = phoneNumber;
    }


    public Boolean updateScrap(Scrap scrap){
        if(this.scraps.contains(scrap)){
            this.scraps.remove(scrap);
            return false;
        }
        this.scraps.add(scrap);
        return true;
    }
}
