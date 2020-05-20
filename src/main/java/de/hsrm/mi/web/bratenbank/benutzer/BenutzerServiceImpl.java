package de.hsrm.mi.web.bratenbank.benutzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BenutzerServiceImpl implements BenutzerService {

    @Autowired
    BenutzerRepository benutzerrepro;

    @Override
    public boolean pruefeLogin(String loginname, String passwort) {
        if (passwort.equals(ermittlePasswort(loginname))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String ermittlePasswort(String loginname) {
        if(findeBenutzer(loginname) != null) {
            return benutzerrepro.findByLoginname(loginname).getPasswort();
        } else {
            return (loginname + loginname.length());
        }
    }

    @Override
    public Benutzer registriereBenutzer(Benutzer neubenutzer) throws BenutzernameSchonVergeben {
        if(findeBenutzer(neubenutzer.getLoginname()) == null) {
            return benutzerrepro.save(neubenutzer);
        } else {
            throw new BenutzernameSchonVergeben();
        }
    }

    @Override
    public Benutzer findeBenutzer(String loginname) {
        try {
            return benutzerrepro.findByLoginname(loginname);
        } catch (Exception e) {
            return null;
        }
    }
    
}