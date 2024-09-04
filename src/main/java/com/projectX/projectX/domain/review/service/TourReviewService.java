package com.projectX.projectX.domain.review.service;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.InvalidMemberException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.domain.review.dto.request.ReviewUpdateRequest;
import com.projectX.projectX.domain.review.dto.response.ReviewGetAllResponse;
import com.projectX.projectX.domain.review.entity.TourReview;
import com.projectX.projectX.domain.review.exception.AlreadyExistReviewException;
import com.projectX.projectX.domain.review.exception.CannotUploadFileException;
import com.projectX.projectX.domain.review.exception.ExceedFileException;
import com.projectX.projectX.domain.review.exception.ReviewNotFoundException;
import com.projectX.projectX.domain.review.repository.TourReviewRepository;
import com.projectX.projectX.domain.review.util.TourReviewMapper;
import com.projectX.projectX.domain.tour.entity.Tour;
import com.projectX.projectX.domain.tour.repository.TourRepository;
import com.projectX.projectX.domain.travel.exception.TravelNotFoundException;
import com.projectX.projectX.domain.work.exception.InvalidPageException;
import com.projectX.projectX.domain.work.exception.NoMorePageException;
import com.projectX.projectX.global.common.RecommendationType;
import com.projectX.projectX.global.common.S3Service;
import com.projectX.projectX.global.exception.ErrorCode;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TourReviewService {

    private final TourRepository tourRepository;
    private final MemberRepository memberRepository;
    private final TourReviewRepository tourReviewRepository;

    private final S3Service s3Service;

    private static final int MAX_REVIEW_IMAGE_SIZE = 5;

    private Tour checkTourExist(Long tourId) {
        return tourRepository.findById(tourId).orElseThrow(
            () -> new TravelNotFoundException(ErrorCode.TRAVEL_NOT_FOUND)
        );
    }

    private Member checkMemberExist(String email) {
        return memberRepository.findByUserEmail(email).orElseThrow(
            () -> new InvalidMemberException(ErrorCode.INVALID_MEMBER_EXCEPTION)
        );
    }

    private TourReview checkReviewExist(Long reviewId) {
        return tourReviewRepository.findById(reviewId).orElseThrow(
            () -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

    private void checkMemberReview(Member member) {
        if (tourReviewRepository.existsByUser(member)) {
            throw new AlreadyExistReviewException(ErrorCode.ALREADY_EXIST_REVIEW);
        }
    }


    private void checkFiles(List<MultipartFile> files) {
        if (files.size() > MAX_REVIEW_IMAGE_SIZE) {
            throw new ExceedFileException(ErrorCode.EXCEED_FILE);
        }
    }

    @Transactional
    public void createTourReview(Long tourId, List<MultipartFile> files, Integer score,
        String contents, RecommendationType recommendationType, String email) {
        Tour tour = checkTourExist(tourId);
        Member member = checkMemberExist(email);
        checkMemberReview(member);
        if (!files.isEmpty()) {
            checkFiles(files);
        }

        Long tourReviewId = createReviewContent(member, tour, score, contents, recommendationType);
        String dirName = "reviews/tour/" + member.getUserEmail();
        createReviewFile(tourReviewId, files, dirName);
    }

    private Long createReviewContent(Member member, Tour tour, Integer score, String contents,
        RecommendationType recommendationType) {
        TourReview tourReview = TourReviewMapper.toTourReview(member, tour, score, contents,
            recommendationType);
        tourReviewRepository.save(tourReview);
        return tourReview.getId();
    }

    private void createReviewFile(Long reviewId, List<MultipartFile> files, String dirName) {
        if (Objects.isNull(files)) {
            return;
        }

        TourReview tourReview = checkReviewExist(reviewId);

        for (int i = 1; i <= files.size(); ++i) {
            try {
                String imageUrl = s3Service.upload(files.get(i - 1), dirName, i);
                tourReview.createReviewImage(imageUrl);
            } catch (IOException e) {
                throw new CannotUploadFileException(ErrorCode.CANNOT_UPLOAD_FILE);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewGetAllResponse> getTourReviewAll(Long travelId, Integer page) {
        Pageable pageable = PageRequest.of(page, 20);
        Tour tour = checkTourExist(travelId);
        Page<TourReview> reviewPage = tourReviewRepository.findByTour(tour, pageable);

        if (reviewPage.isEmpty()) {
            if (page >= 0) {
                throw new NoMorePageException(ErrorCode.NO_MORE_PAGE);
            }
            throw new InvalidPageException(ErrorCode.INVALID_PAGE);
        }

        List<ReviewGetAllResponse> reviewList = TourReviewMapper.toTourReviewGetAllResponse(
            reviewPage);

        if (reviewList.isEmpty()) {
            throw new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return reviewList;
    }

    @Transactional
    public void updateTourReview(Long travelId, Long reviewId, ReviewUpdateRequest request,
        String userEmail) {
        checkTourExist(travelId);
        checkMemberExist(userEmail);
        TourReview tourReview = checkReviewExist(reviewId);
        tourReview.updateReview(request);
    }

}
