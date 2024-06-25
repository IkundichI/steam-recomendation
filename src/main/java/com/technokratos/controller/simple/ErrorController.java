package com.technokratos.controller.simple;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(@RequestParam(value = "message", required = false) String message, Model model) {
        if (message == null) {
            message = "An unexpected error occurred. Please try again later.";
        }
        model.addAttribute("message", message);
        return "error";
    }
}
