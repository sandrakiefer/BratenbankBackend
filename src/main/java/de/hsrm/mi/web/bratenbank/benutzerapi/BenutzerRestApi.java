package de.hsrm.mi.web.bratenbank.benutzerapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.bratenbank.benutzer.Benutzer;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerService;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzernameSchonVergeben;

@RestController
@RequestMapping("/api")
public class BenutzerRestApi {

    @Autowired
    BenutzerService benutzerService;

    @PostMapping(value = "/benutzer", produces = MediaType.APPLICATION_JSON_VALUE)
    public Benutzer postNeuerBenutzer(@RequestBody Benutzer benutzer) throws BenutzerApiException {
        try {
            return benutzerService.registriereBenutzer(benutzer);
        } catch (BenutzernameSchonVergeben e) {
            throw new BenutzerApiException();
        }
    }

    @GetMapping(value = "/benutzer/{loginname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Benutzer getBenutzer(@PathVariable("loginname") String loginname) throws BenutzerApiException {
        Benutzer b = benutzerService.findeBenutzer(loginname);
        if (b == null) {
            throw new BenutzerApiException();
        } else {
            return b;
        }
    }

}
