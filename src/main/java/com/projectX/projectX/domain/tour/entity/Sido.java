package com.projectX.projectX.domain.tour.entity;

import com.projectX.projectX.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Sido extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("시도 식별자")
    private Long id;

    @Column(nullable = false)
    @Comment("시도 코드")
    private Integer sidoCode;


    @Column(nullable = false)
    @Comment("시도 이름")
    private String sidoName;


    @Builder
    public Sido(Long id, Integer sidoCode, String sidoName) {
        this.id = id;
        this.sidoCode = sidoCode;
        this.sidoName = sidoName;
    }
}
