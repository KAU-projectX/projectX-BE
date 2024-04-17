package com.projectX.projectX.domain.member.service;

import com.projectX.projectX.domain.member.dto.request.MemberSignUpRequest;
import com.projectX.projectX.domain.member.dto.response.MemberSignUpResponse;
import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.exception.AlreadyExistIdException;
import com.projectX.projectX.domain.member.exception.InvalidPasswordException;
import com.projectX.projectX.domain.member.repository.MemberRepository;
import com.projectX.projectX.domain.member.util.MemberMapper;
import com.projectX.projectX.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberSignUpResponse memberSignUp(final MemberSignUpRequest memberSignUpRequest) {
        isDuplicateMember(memberSignUpRequest);
        checkPassword(memberSignUpRequest.userId(), memberSignUpRequest.userPassword());
        return MemberMapper.toMemberSignUpResponse(
            memberRepository.save(MemberMapper.toMember(memberSignUpRequest))
        );
    }

    private void isDuplicateMember(MemberSignUpRequest memberSignUpRequest) {
        if (memberRepository.findByUserId(memberSignUpRequest.userId())) {
            throw new AlreadyExistIdException(ErrorCode.ALREADY_EXIST_ID_EXCEPTION);
        }
    }

    private void checkPassword(String id, String password){
        if(id.equals(password)){
            throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD_EXCEPTION);
        }
    }

}
