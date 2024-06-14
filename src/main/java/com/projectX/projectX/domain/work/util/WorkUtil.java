package com.projectX.projectX.domain.work.util;

import com.projectX.projectX.domain.cafe.entity.Cafe;
import com.projectX.projectX.domain.work.dto.response.WorkGetAllResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkUtil {

    public static WorkGetAllResponse toWorkGetAllResponse(Cafe cafe) {
        return WorkGetAllResponse.builder()
            .id(cafe.getId())
            .name(cafe.getName())
            .address(cafe.getAddress())
            .phone(cafe.getPhoneNumber())
            .build();
    }

}
