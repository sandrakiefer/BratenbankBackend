package de.hsrm.mi.web.bratenbank.benutzer.benutzerui;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import de.hsrm.mi.web.bratenbank.benutzer.Benutzer;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerService;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzernameSchonVergeben;

@Controller
public class BenutzerController {
    @Autowired BenutzerService benutzerservice;
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);
    
    @GetMapping("/benutzer")
    public String benutzer_get(Model m) {
        m.addAttribute("benutzer", new Benutzer());
        return "benutzerui/benutzerregistrierung";
    }

    @PostMapping("/benutzer")
    public String postBenutzer(Model m, @Valid @ModelAttribute("benutzer") Benutzer benutzer, BindingResult benutzerError) {
        if (benutzerError.hasErrors() || !benutzer.isNutzungsbedingungenok()) {
            return "benutzerui/benutzerregistrierung";
        } 
        try {
            benutzerservice.registriereBenutzer(benutzer);
            return "login";
        } catch (BenutzernameSchonVergeben e) {
            benutzer.setLoginname("");
            return "benutzerui/benutzerregistrierung";
        }
    }

}
