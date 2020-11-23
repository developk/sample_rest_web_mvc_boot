package com.gru.erpdm.rest.controller;

import com.gru.erpdm.vo.Greeting;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template_str = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/greeting")
    public Greeting greeting(String name) {
        System.out.println("=== in greeting ===");
        return new Greeting(counter.incrementAndGet(), String.format(template_str, name));
    }
}
