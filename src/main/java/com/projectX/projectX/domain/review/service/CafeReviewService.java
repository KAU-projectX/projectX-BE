package com.projectX.projectX.domain.review.service;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.InvalidMemberException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.domain.review.dto.request.ReviewUpdateRequest;
import com.projectX.projectX.domain.review.dto.response.ReviewGetAllResponse;
import com.projectX.projectX.domain.review.entity.CafeReview;
import com.projectX.projectX.domain.review.exception.AlreadyExistReviewException;
import com.projectX.projectX.domain.review.exception.CannotUploadFileException;
import com.projectX.projectX.domain.review.exception.ExceedFileException;
import com.projectX.projectX.domain.review.exception.ReviewNotFoundException;
import com.projectX.projectX.domain.review.repository.CafeReviewRepository;
import com.projectX.projectX.domain.review.util.CafeReviewMapper;
import com.projectX.projectX.domain.work.exception.InvalidPageException;
import com.projectX.projectX.domain.work.exception.NoMorePageException;
import com.projectX.projectX.domain.work.exception.WorkNotFoundException;
import com.projectX.projectX.global.common.RecommendationType;
import com.projectX.projectX.global.common.S3Service;
import com.projectX.projectX.global.exception.ErrorCode;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final S3Service s3Service;

    private static final int MAX_REVIEW_IMAGE_SIZE = 5;

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

    private CafeReview checkReviewExist(Long cafeReviewId) {
        return cafeReviewRepository.findById(cafeReviewId).orElseThrow(
            () -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

    private void checkMemberReview(Member member, Cafe cafe) {
        if (cafeReviewRepository.existsByUserAndCafe(member, cafe)) {
            throw new AlreadyExistReviewException(ErrorCode.ALREADY_EXIST_REVIEW);
        }
    }

    private void checkFiles(List<MultipartFile> files) {
        if (Objects.isNull(files)) {
            return;
        }
        if (files.size() > MAX_REVIEW_IMAGE_SIZE) {
            throw new ExceedFileException(ErrorCode.EXCEED_FILE);
        }
    }

    @Transactional
    public void createCafeReview(Long cafeId, List<MultipartFile> files, Integer score,
        String contents, RecommendationType recommendationType, String email) {
        Cafe cafe = checkCafeExist(cafeId);
        Member member = checkMemberExist(email);
        checkMemberReview(member, cafe);
        checkFiles(files);

        Long cafeReviewId = createReviewContent(member, cafe, score, contents, recommendationType);

        String dirName = "reviews/cafe/" + member.getUserEmail();
        createReviewFile(cafeReviewId, files, dirName);
    }

    private Long createReviewContent(Member member, Cafe cafe, Integer score, String contents,
        RecommendationType recommendationType) {
        CafeReview cafeReview = CafeReviewMapper.toCafeReview(member, cafe, score, contents,
            recommendationType);
        cafeReviewRepository.save(cafeReview);
        return cafeReview.getId();
    }

    private void createReviewFile(Long cafeReviewId, List<MultipartFile> files, String dirName) {
        if (Objects.isNull(files)) {
            return;
        }

        CafeReview cafeReview = checkReviewExist(cafeReviewId);

        for (int i = 1; i <= files.size(); ++i) {
            try {
                String imageUrl = s3Service.upload(files.get(i - 1), dirName, i);
                cafeReview.createReviewImage(imageUrl);
            } catch (IOException e) {
                throw new CannotUploadFileException(ErrorCode.CANNOT_UPLOAD_FILE);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewGetAllResponse> getCafeReviewAll(Long cafeId, Integer page) {
        Pageable pageable = PageRequest.of(page, 20);
        Cafe cafe = checkCafeExist(cafeId);
        Page<CafeReview> reviewPage = cafeReviewRepository.findByCafe(cafe, pageable);

        if (reviewPage.isEmpty()) {
            if (page >= 0) {
                throw new NoMorePageException(ErrorCode.NO_MORE_PAGE);
            }
            throw new InvalidPageException(ErrorCode.INVALID_PAGE);
        }

        List<ReviewGetAllResponse> reviewList = CafeReviewMapper.toCafeReviewGetAllResponse(
            reviewPage);

        if (reviewList.isEmpty()) {
            throw new WorkNotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return reviewList;
    }

    @Transactional
    public void updateWorkReview(Long cafeId, Long reviewId, ReviewUpdateRequest request,
        String userEmail) {
        checkCafeExist(cafeId);
        checkMemberExist(userEmail);
        CafeReview cafeReview = checkReviewExist(reviewId);
        cafeReview.updateReview(request);
    }

}
