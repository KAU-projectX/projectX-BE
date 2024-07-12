package com.projectX.projectX.domain.cafe.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

    @EmbeddedId
    private UserScrapCafe id;

    public Scrap(UserScrapCafe userScrapCafe){
        this.id = userScrapCafe;
    }

}
