package de.hsrm.mi.web.bratenbank.bratrepo;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import de.hsrm.mi.web.bratenbank.benutzer.Benutzer;
import de.hsrm.mi.web.bratenbank.validation.GuteAdresse;

@Entity
public class Braten {

    @GuteAdresse
    @NotNull(message = "Abholort muss angegeben werden")
    private String abholort;

    @NotNull(message = "Datum muss angegeben werden")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate haltbarbis;

    @Size(min=1, max=80, message = "Beschreibung darf nicht länger als {max} Zeichen haben")
    @NotNull(message = "Beschreibung muss angegeben werden")
    private String beschreibung;

    @JsonIgnore
    private int[] selectboxes = {0, 25, 50, 75, 100};

    @NotNull(message = "Vegetarizität muss angegeben werden")
    private int vgrad;

    @Id
    @GeneratedValue
    private int id;

    @Version
    private int version;

    @ManyToOne
    private Benutzer anbieter;

    public Braten(String abholort, LocalDate haltbarbis, String beschreibung, int vgrad) {
        this.abholort = abholort;
        this.haltbarbis = haltbarbis;
        this.beschreibung = beschreibung;
        this.vgrad = vgrad;
    }

    public Braten() {}

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

    public int getVgrad() {
        return vgrad;
    }

    public void setVgrad(int vgrad) {
        this.vgrad = vgrad;
    }

    public int[] getSelectboxes() {
        return selectboxes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Benutzer getAnbieter() {
        return anbieter;
    }

    public void setAnbieter(Benutzer anbieter) {
        this.anbieter = anbieter;
    }

    @Override
    public String toString() {
        return String.format("Abholort: '%s'; HaltbarBis: '%s', Beschreibung: '%s'", abholort, haltbarbis, beschreibung);
    }

}
