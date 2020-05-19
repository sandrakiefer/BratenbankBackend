package de.hsrm.mi.web.bratenbank.benutzer;

import org.springframework.stereotype.Service;

@Service
public class BenutzerServiceImpl implements BenutzerService {

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
        return (loginname + loginname.length());
    }
    
}