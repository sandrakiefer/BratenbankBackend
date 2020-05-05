package de.hsrm.mi.web.skief001.bratboerse;

import java.time.LocalDate;

public class BratenDaten {
    
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

    public LocalDate getHaltbarbis() {
        return haltbarbis;
    }

    public void setHaltbarbis(LocalDate haltbarbis) {
        this.haltbarbis = haltbarbis;
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
