package com.projectX.projectX.domain.member.repository;

import com.projectX.projectX.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean findByUserId(String userId);
}
