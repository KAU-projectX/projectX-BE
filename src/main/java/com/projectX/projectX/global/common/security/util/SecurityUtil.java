package com.projectX.projectX.global.common.security.util;

import com.projectX.projectX.global.common.security.dto.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtil {

    public static String getUserEmail() {
        return ((SecurityUser) (SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal())).email();
    }

    public static SecurityUser getUser() {
        return (SecurityUser) (SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal());
    }

}
