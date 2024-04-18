package com.projectX.projectX.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/sample")
    public String testController(){
        return "Sample!";
    }
}
