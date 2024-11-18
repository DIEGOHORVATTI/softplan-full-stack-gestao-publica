package com.rinha.backend.service;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {
  public String getHelloWorld(String name) {
    return "Hello World, " + name + "!";
  }
}
