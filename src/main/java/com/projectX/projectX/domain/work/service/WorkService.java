package com.projectX.projectX.domain.work.service;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.cafe.repository.CafeRepository;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.InvalidMemberException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetDetailResponse;
import com.projectX.projectX.domain.work.dto.response.WorkGetRecommdResponse;
import com.projectX.projectX.domain.work.exception.InvalidPageException;
import com.projectX.projectX.domain.work.exception.NoMorePageException;
import com.projectX.projectX.domain.work.exception.WorkNotFoundException;
import com.projectX.projectX.domain.work.util.WorkMapper;
import com.projectX.projectX.global.common.CafeType;
import com.projectX.projectX.global.common.JejuRegion;
import com.projectX.projectX.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;

    private static final int RECOMMEND_WORK_SIZE = 3;
    private static final long CANNOT_RECOMMEND_CAFE = 0;

    private Cafe isExistCafe(Long cafeId){
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
            () -> new WorkNotFoundException(ErrorCode.WORK_NOT_FOUND)
        );

        return cafe;
    }

    private Member isExistMember(String userEmail){
        Member member = memberRepository.findByUserEmail(userEmail).orElseThrow(
            () -> new InvalidMemberException(ErrorCode.INVALID_MEMBER_EXCEPTION)
        );

        return member;
    }

    @Transactional(readOnly = true)
    public List<WorkGetAllResponse> getWorkAllInfo(Integer page, Integer cafeType, Integer jejuRegion,
        String franchiseName) {
        Page<Cafe> workPage;
        Pageable pageable = PageRequest.of(page, 20);

        if (jejuRegion == null && franchiseName == null) {
            workPage = cafeRepository.findByCafeType(CafeType.fromInt(cafeType), pageable);
        } else if (jejuRegion != null && franchiseName == null) {
            workPage = cafeRepository.findByCafeTypeAndJejuRegion(CafeType.fromInt(cafeType),
                JejuRegion.fromInt(jejuRegion), pageable);
        } else if (jejuRegion == null && franchiseName != null) {
            workPage = cafeRepository.findByCafeType(CafeType.FRANCHISE, pageable);
        } else {
            workPage = cafeRepository.findByCafeTypeAndJejuRegion(CafeType.FRANCHISE,
                JejuRegion.fromInt(jejuRegion), pageable);
        }

        if (workPage.isEmpty()) {
            if (page >= 0) {
                throw new NoMorePageException(ErrorCode.NO_MORE_PAGE);
            }
            throw new InvalidPageException(ErrorCode.INVALID_PAGE);
        }

        List<WorkGetAllResponse> cafeList = new ArrayList<>();
        for (Cafe cafe : workPage.toList()) {
            WorkGetAllResponse workGetAllResponse = WorkMapper.toWorkGetAllResponse(cafe);
            cafeList.add(workGetAllResponse);
        }

        if (cafeList.isEmpty()) {
            throw new WorkNotFoundException(ErrorCode.WORK_NOT_FOUND);
        }

        return cafeList;
    }


    @Transactional(readOnly = true)
    public WorkGetDetailResponse getWorkDetailInfo(Long cafeId) {
        Cafe cafe = isExistCafe(cafeId);
        return WorkMapper.toWorkGetDetailResponse(cafe);
    }

    @Transactional
    public String postWorkScrapInfo(Long cafeId, String userEmail) {
        Cafe cafe = isExistCafe(cafeId);
        Member member = isExistMember(userEmail);

        Boolean result = cafe.updateScrap(cafe, member);
        return result ? "work 정보를 스크랩했습니다." : "work 스크랩을 취소했습니다.";
    }

    @Transactional(readOnly = true)
    public List<WorkGetRecommdResponse> getWorkRecommendInfo(JejuRegion jejuRegion) {
        Random random = new Random();
        Set<Long> set = new HashSet<>();
        List<Cafe> cafes = new ArrayList<>();

        long maxPage = cafeRepository.countByJejuRegion(jejuRegion);

        if (Objects.equals(maxPage, CANNOT_RECOMMEND_CAFE)) {
            return new ArrayList<>();
        }

        if (maxPage < RECOMMEND_WORK_SIZE) {
            List<Cafe> cafe = cafeRepository.findByJejuRegion(jejuRegion);
            return WorkMapper.toWorkGetRecommendResponse(cafe);
        }

        while (cafes.size() < RECOMMEND_WORK_SIZE) {
            long randomPage = random.nextLong(maxPage);
            if (set.contains(randomPage)) {
                continue;
            }

            set.add(randomPage);
            Pageable pageable = PageRequest.of((int) randomPage, 1);
            Page<Cafe> cafe = cafeRepository.findByJejuRegion(jejuRegion, pageable);
            cafes.add(cafe.getContent().get(0));
        }

        return WorkMapper.toWorkGetRecommendResponse(cafes);
    }


}
