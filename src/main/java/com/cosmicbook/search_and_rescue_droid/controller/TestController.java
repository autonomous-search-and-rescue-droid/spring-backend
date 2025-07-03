package com.cosmicbook.search_and_rescue_droid.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public content accessible to all.";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "User content — requires USER or higher.";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Admin content — requires ADMIN role.";
    }

    @GetMapping("/common")
    public String commonAccess() {
        return "Common content — requires COMMON role.";
    }
}
