package com.projectX.projectX.docker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerRestController {

    @RequestMapping("/sample")
    public String docker() {
        return "docker sample";
    }
}
