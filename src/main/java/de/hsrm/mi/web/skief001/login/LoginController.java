package de.hsrm.mi.web.skief001.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String login_get() {
        return "login";
    }

}
