package com.merca.merca.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootRedirectController {
    
    @GetMapping("/")
    public RedirectView redirectToContextPath() {
        return new RedirectView("/mercadia/");
    }
}
