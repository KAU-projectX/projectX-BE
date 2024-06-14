package com.projectX.projectX.global.security.service;

import com.projectX.projectX.domain.member.entity.Member;
import com.projectX.projectX.domain.member.entity.ProviderType;
import com.projectX.projectX.domain.member.entity.RoleType;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE) //Builder 메서드를 외부에서 사용하지 않으므로, private 지정
@Getter
public class OAuthAttribute {

    private String attributeKey; //사용자 속성의 키
    private Map<String, Object> attributes; //사용자 속성 정보를 담는 Map
    private String nickname;
    private String email;
    private String provider; //제공자 정보

    //서비스에 따라 OAuth2Attribute 객체를 생성하는 메서드
    static OAuthAttribute of(String provider, String attributeKey,
        Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return ofKakao(provider, attributeKey, attributes);
            default:
                throw new RuntimeException();
        }
    }

    //카카오 로그인은 kakaoAccount -> kakaoProfile로 2번 감싸져 있으므로 두번의 get() 메서드가 필요하다.
    private static OAuthAttribute ofKakao(String provider, String attributeKey,
        Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttribute.builder()
            .nickname((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .attributes(kakaoAccount)
            .attributeKey(attributeKey)
            .provider(provider)
            .build();
    }

    //OAuth2User 객체에 넣어주기 위해 Map으로 값들을반환한다.
    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("provider", provider);
        map.put("nickname", nickname);
        map.put("email", email);
        return map;
    }

    //Member 객체를 만든다.
    public Member toEntity() {
        return Member.builder()
            .userNickName(nickname)
            .userEmail(email)
            .providerType(ProviderType.toProviderType(provider))
            .userRole(RoleType.USER)
            .build();
    }

}
