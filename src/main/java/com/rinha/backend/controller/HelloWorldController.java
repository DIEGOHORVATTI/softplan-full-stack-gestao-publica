package com.rinha.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rinha.backend.domain.User;
import com.rinha.backend.service.HelloWorldService;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

  @Autowired
  private HelloWorldService helloWorldService;

  @GetMapping
  public String HelloWord(@RequestParam(value = "filter", defaultValue = "") String filter) {
    return helloWorldService.getHelloWorld("filtro -> " + filter);
  }

  @PostMapping
  public String HelloWordPost(@RequestBody User body) {
    return "hello " + body.getName() + " de idade " + body.getAge();
  }

  @PostMapping("/{id}")
  public String HelloWordId(@PathVariable String id, @RequestBody User body) {
    return "hello user: " + id + " vulgo: " + body.getName() + " de idade " + body.getAge();
  }
}
