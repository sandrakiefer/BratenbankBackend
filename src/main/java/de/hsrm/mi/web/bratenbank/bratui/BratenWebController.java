package de.hsrm.mi.web.bratenbank.bratui;

import java.util.ArrayList;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.bratenbank.bratboerse.BratenDaten;
import de.hsrm.mi.web.bratenbank.bratrepo.Braten;
import de.hsrm.mi.web.bratenbank.bratservice.BratenService;
import de.hsrm.mi.web.bratenbank.bratservice.BratenServiceException;

@Controller
@RequestMapping("/braten")
@SessionAttributes("loggedinusername")
public class BratenWebController {
    
    @Autowired BratenService bratenservice;
    Logger logger = LoggerFactory.getLogger(BratenWebController.class);

    @GetMapping("/angebot")
    public String angebot_get(Model m) {
        m.addAttribute("angebote", bratenservice.alleBraten());
        return "braten/liste";
    }

    @GetMapping("/angebot/neu")
    public String getForm(Model m) {
        m.addAttribute("angebotform", new Braten());
        return "braten/bearbeiten";
    }

    @PostMapping("/angebot/neu")
    public String postForm(Model m, @Valid @ModelAttribute("angebotform") Braten angebotform, BindingResult bratendatenError) {
        if (bratendatenError.hasErrors()) {
            return "braten/bearbeiten";
        }
        String username = (String) m.getAttribute("loggedinusername");
        try {
            bratenservice.editBraten(username, angebotform);
        } catch (BratenServiceException e) {
            return "braten/bearbeiten";
        }
        return "redirect:/braten/angebot";
    }

    @GetMapping("/angebot/{id}/del")
    public String delete(@ModelAttribute("angebote") ArrayList<BratenDaten> angebote, @PathVariable int id) {
        bratenservice.loescheBraten(id);
        return "redirect:/braten/angebot";
    }

    @GetMapping("/angebot/{id}")
    public String edit(Model m, @ModelAttribute("angebote") ArrayList<BratenDaten> angebote, @PathVariable int id) {
        if (bratenservice.sucheBratenMitId(id).isPresent()) {
            m.addAttribute("angebotform", bratenservice.sucheBratenMitId(id).get());
            return "braten/bearbeiten";
        } else {
            return "redirect:/braten/angebot";
        }
        
    }

}
