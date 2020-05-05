package de.hsrm.mi.web.skief001.bratboerse;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BratenDaten {

    Logger logger = LoggerFactory.getLogger(BratenDaten.class);
    
    private String name;
    private String abholort;
    private LocalDate haltbarbis;
    private String beschreibung;

    public BratenDaten(String name, String abholort, LocalDate haltbarbis, String beschreibung) {
        this.name = name;
        this.abholort = abholort;
        this.haltbarbis = haltbarbis;
        this.beschreibung = beschreibung;
    }

    public BratenDaten() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbholort() {
        return abholort;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
    }

    public String getHaltbarbis() {
        return haltbarbis.toString();
    }

    public void setHaltbarbis(String haltbarbis) {
        this.haltbarbis = LocalDate.parse(haltbarbis);
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        return String.format("Name: '%s'; Abholort: '%s'; HaltbarBis: '%s', Beschreibung: '%s'", name, abholort, haltbarbis.toString(), beschreibung);
    }

}
