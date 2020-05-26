package de.hsrm.mi.web.bratenbank.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.bratenbank.benutzer.BenutzerService;

@Controller
@SessionAttributes(names = {"loggedinusername"})
public class LoginController {
    @Autowired BenutzerService benutzerservice;
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public static String login_get() {
        return "login";
    }

    @PostMapping("/login")
    public String login_post(Model m, @RequestParam String username, @RequestParam String password) {
        if(benutzerservice.pruefeLogin(username, password)) {
            m.addAttribute("loggedinusername", username);
            if (username.contains("alt")) {
                return "redirect:/angebot";
            } else {
                return "redirect:/braten/angebot";
            }
        } else {
            m.addAttribute("hinweis", String.format("Hinweis: Das korrekte Passwort für %s ist %s, nicht %s!", username, benutzerservice.ermittlePasswort(username), password));
            return "login";
        }
    }

}
