package com.projectX.projectX.domain.member.entity;

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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 식별자")
    private Long id;

    @Column(length = 15, nullable = false)
    @Comment("회원 아이디")
    private String userId;

    @Column(length = 15, nullable = false)
    @Comment("회원 비밀번호")
    private String userPassword;


    @Column(length = 15, nullable = false)
    @Comment("회원 닉네임")
    private String userNickName;


    @Column(length = 25)
    @Comment("회원 전화번호")
    private String userPhoneNumber;

    @Builder
    public Member(Long id, String userId, String userPassword, String userNickName,
        String userPhoneNumber) {
        this.id = id;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userNickName = userNickName;
        this.userPhoneNumber = userPhoneNumber;
    }
}
