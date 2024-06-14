package com.projectX.projectX.domain.member.entity;

import com.projectX.projectX.global.common.BaseEntity;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 식별자")
    private Long id;

    @Column(length = 40, nullable = false)
    @Comment("회원 이메일")
    private String userEmail;

    @Column(length = 15, nullable = false)
    @Comment("회원 닉네임")
    private String userNickName;

    @Enumerated(EnumType.STRING)
    @Comment("회원 역할")
    private RoleType userRole;

    @Enumerated(EnumType.STRING)
    @Comment("회원 외부 계정")
    private ProviderType providerType;

    @Builder
    public Member(Long id, String userEmail, String userNickName, RoleType userRole,
        ProviderType providerType) {
        this.id = id;
        this.userEmail = userEmail;
        this.userNickName = userNickName;
        this.userRole = userRole;
        this.providerType = providerType;
    }

    public Member update(String userNickName, String userEmail, ProviderType providerType,
        RoleType userRole) {
        this.userNickName = userNickName;
        this.userEmail = userEmail;
        this.providerType = providerType;
        this.userRole = userRole;
        return this;
    }
}
