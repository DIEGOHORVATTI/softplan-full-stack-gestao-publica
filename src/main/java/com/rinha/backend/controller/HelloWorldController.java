package com.rinha.backend.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HelloWorldController {

    @GetMapping
    public String GetApiStatus() {
        return "API Running 🚀";
    }

}