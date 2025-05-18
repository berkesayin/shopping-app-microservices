package dev.berke.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/all")
    public String testAllAccess(){
        return "Public Content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String testUserAccess() {
        return "User Board";
    }

    @GetMapping("/backoffice")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public String testBackofficeAccess() {
        return "Backoffice Board";
    }

    @GetMapping("/user-and-backoffice")
    @PreAuthorize("hasRole('USER') or hasRole('BACKOFFICE')")
    public String testUserAndBackofficeAccess() {
        return "User and Backoffice Board";
    }
}
