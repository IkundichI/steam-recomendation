package com.technokratos.controller.simple;

import com.technokratos.record.SignUpForm;
import com.technokratos.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/signUp")
    public String getSignUpPage() {
        return "sign_up";
    }

    @PostMapping("/signUp")
    public String signUpUser(SignUpForm signUpForm, ModelMap modelMap) {

        if (!signUpService.signUp(signUpForm)) {
            modelMap.addAttribute("error", "По вашему steamId не найден пользователь. Повторите попытку");
            return "sign_up";
        }

        return "redirect:/signIn";
    }
}
