package de.hsrm.mi.web.skief001.bratboerse;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import de.hsrm.mi.web.skief001.validation.GuteAdresse;

public class BratenDaten {
    
    @Size(min=3, max=80, message = "Name muss mindestens {min} Zeichen und maximal {max} Zeichen haben")
    @NotNull(message="Name muss angegeben werden")
    private String name;

    @GuteAdresse
    @NotNull(message = "Abholort muss angegeben werden")
    private String abholort;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    @NotNull(message = "Datum muss angegeben werden")
    private LocalDate haltbarbis;

    @Size(min=1, max=80, message = "Beschreibung darf nicht länger als {max} Zeichen haben")
    @NotNull(message = "Beschreibung muss angegeben werden")
    private String beschreibung;

    Logger logger = LoggerFactory.getLogger(BratenDaten.class);

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
