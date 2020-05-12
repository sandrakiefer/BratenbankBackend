package de.hsrm.mi.web.skief001.bratboerse;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = {"angebote"})
public class BratenAngebotController {

    Logger logger = LoggerFactory.getLogger(BratenAngebotController.class);
    ArrayList<BratenDaten> angebote = new ArrayList<BratenDaten>();
    
    @ModelAttribute("angebote")
    public void initListe(Model m) {
        angebote.add(new BratenDaten("Jöndhard", "Vollradisroda", LocalDate.of(2020, 5, 6), "leckerer Grünkohlbraten"));
        angebote.add(new BratenDaten("Friedfert", "Arensch", LocalDate.of(2020, 5, 31), "Palatschinken aus Öl"));
        angebote.add(new BratenDaten("Joghurta", "Diedelingen", LocalDate.of(2020, 5, 7), "frischer Gummibärbraten"));
        m.addAttribute("angebote", angebote);
    }

    @GetMapping("/angebot")
    public String angebot_get() {
        return "angebote/liste";
    }

    @GetMapping("/angebot/neu")
    public String getForm(Model m) {
        m.addAttribute("angebotform", new BratenDaten());
        return "angebote/bearbeiten";
    }

    @PostMapping("/angebot/neu")
    public String postFrom(Model m, @Valid @ModelAttribute("angebotform") BratenDaten angebotform, BindingResult bratendatenError) {
        if (bratendatenError.hasErrors()) {
            return "angebote/bearbeiten";
        }
        angebote.add(angebotform);
        return "redirect:/angebot";
    }

    @GetMapping("/angebot/{id}/del")
    public String delete(@PathVariable int id) {
        angebote.remove(id-1);
        return "redirect:/angebot";
    }

    @GetMapping("/angebot/{id}")
    public String edit(Model m, @PathVariable int id) {
        m.addAttribute("angebotform", angebote.get(id-1));
        angebote.remove(id-1);
        return "angebote/bearbeiten";
    }

}
