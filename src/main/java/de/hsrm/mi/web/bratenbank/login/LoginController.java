package de.hsrm.mi.web.bratenbank.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login_get() {
        return "login";
    }

    @PostMapping("/login")
    public String login_post(Model m, @RequestParam String username, @RequestParam String password) {
        String correct = username + username.length();
        if (password.equals(correct)) {
            return "redirect:/angebot";
        } else {
            m.addAttribute("hinweis", String.format("Hinweis: Das korrekte Passwort für %s ist %s, nicht %s!", username, correct, password));
            return "login";
        }
    }

}
