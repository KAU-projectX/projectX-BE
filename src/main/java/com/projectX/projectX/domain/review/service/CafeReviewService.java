package com.projectX.projectX.domain.review.service;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.InvalidMemberException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.domain.review.entity.CafeReview;
import com.projectX.projectX.domain.review.exception.AlreadyExistCafeReviewException;
import com.projectX.projectX.domain.review.exception.ExceedFileException;
import com.projectX.projectX.domain.review.repository.CafeReviewRepository;
import com.projectX.projectX.domain.review.util.ReviewMapper;
import com.projectX.projectX.domain.work.exception.WorkNotFoundException;
import com.projectX.projectX.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeReviewService {

    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;
    private final CafeReviewRepository cafeReviewRepository;

    private Cafe checkCafeExist(Long cafeId) {
        return cafeRepository.findById(cafeId).orElseThrow(
            () -> new WorkNotFoundException(ErrorCode.WORK_NOT_FOUND)
        );
    }

    private Member checkMemberExist(String email) {
        return memberRepository.findByUserEmail(email).orElseThrow(
            () -> new InvalidMemberException(ErrorCode.INVALID_MEMBER_EXCEPTION)
        );
    }

    private void checkMemberReview(Member member) {
        if (cafeReviewRepository.existsByUser(member)) {
            throw new AlreadyExistCafeReviewException(ErrorCode.ALREADY_EXIST_CAFE_REVIEW);
        }
    }

    private void checkFiles(List<MultipartFile> files) {
        if (files.size() > 5) {
            throw new ExceedFileException(ErrorCode.EXCEED_FILE);
        }
    }

    @Transactional
    public void createCafeReview(Long cafeId, List<MultipartFile> files, Integer score,
        String contents, String email) {
        Cafe cafe = checkCafeExist(cafeId);
        Member member = checkMemberExist(email);
        checkMemberReview(member);
        checkFiles(files);

        createReviewContent(member, cafe, score, contents);
    }

    private Long createReviewContent(Member member, Cafe cafe, Integer score, String contents) {
        CafeReview cafeReview = ReviewMapper.toCafeReview(member, cafe, score, contents);
        cafeReviewRepository.save(cafeReview);
        return cafeReview.getId();
    }
}
