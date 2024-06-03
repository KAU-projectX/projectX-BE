package com.projectX.projectX.domain.cafe.service;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeService {

    @Value("${kakao.key}")
    private String kakao_key;
    @Value("${kakao.base-url}")
    private String kakao_base_url;

    private HttpEntity<String> httpEntity;

    @PostConstruct
    protected void init() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, kakao_key);
        httpEntity = new HttpEntity<>(headers);
    }

    @Transactional
    public boolean isPlaceExist(String name) {
        URI checkURI = UriComponentsBuilder.fromHttpUrl(kakao_base_url)
            .queryParam("query", name)
            .queryParam("page", 1)
            .encode(StandardCharsets.UTF_8)
            .build().toUri();

        Assert.notNull(name, "query");
        ResponseEntity<String> cafeResponse = new RestTemplate().exchange(checkURI, HttpMethod.GET,
            httpEntity, String.class);

        JSONObject jsonObject = new JSONObject(cafeResponse.getBody().toString());
        JSONArray jsonArray = jsonObject.getJSONArray("documents");
        if (jsonArray.length() > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public String getPlaceURI(String name) {
        URI cafeURI = UriComponentsBuilder.fromHttpUrl(kakao_base_url)
            .queryParam("query", name)
            .queryParam("page", 1)
            .encode(StandardCharsets.UTF_8)
            .build().toUri();

        Assert.notNull(name, "query");
        ResponseEntity<String> cafeResponse = new RestTemplate().exchange(cafeURI, HttpMethod.GET,
            httpEntity, String.class);

        JSONObject jsonObject = new JSONObject(cafeResponse.getBody().toString());
        JSONArray jsonArray = jsonObject.getJSONArray("documents");

        JSONObject targetObject = jsonArray.getJSONObject(0);
        String uri = targetObject.getString("place_url");
        return uri;
    }

}
