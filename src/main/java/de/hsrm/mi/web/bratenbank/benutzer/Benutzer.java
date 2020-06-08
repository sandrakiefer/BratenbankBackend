package de.hsrm.mi.web.bratenbank.benutzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import de.hsrm.mi.web.bratenbank.bratrepo.Braten;

@Entity
public class Benutzer {
    
    @NotEmpty
    @Column(unique=true)
    private String loginname;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Size(min=3)
    @NotEmpty
    private String passwort;

    @NotEmpty
    private String vollname;

    private boolean nutzungsbedingungenok;

    @JsonIgnore
    @OneToMany(mappedBy = "anbieter")
    private List<Braten> angebote = new ArrayList<Braten>();

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @Version
    private long version;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getVollname() {
        return vollname;
    }

    public void setVollname(String vollname) {
        this.vollname = vollname;
    }

    public boolean isNutzungsbedingungenok() {
        return nutzungsbedingungenok;
    }

    public void setNutzungsbedingungenok(boolean nutzungsbedingungenok) {
        this.nutzungsbedingungenok = nutzungsbedingungenok;
    }

    public List<Braten> getAngebote() {
        return angebote;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Benutzer [loginname=" + loginname + ", nutzungsbedingungenok=" + nutzungsbedingungenok + ", passwort="
                + passwort + ", vollname=" + vollname + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((loginname == null) ? 0 : loginname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Benutzer other = (Benutzer) obj;
        if (loginname == null) {
            if (other.loginname != null)
                return false;
        } else if (!loginname.equals(other.loginname))
            return false;
        return true;
    }

}
