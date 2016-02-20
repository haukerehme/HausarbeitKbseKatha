/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Null;

/**
 *
 * @author Katha
 */
@Entity
public class Schrank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int schranknummer;
    
    @OneToOne
    private Kunde kunde;

    public int getSchranknummer() {
        return schranknummer;
    }

    public void setSchranknummer(int schranknummer) {
        this.schranknummer = schranknummer;
    }

    public Schrank(Kunde kunde) {
        this.kunde = kunde;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Schrank)) {
            return false;
        }
        Schrank other = (Schrank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Schrank Nr. " + id + " ]";
    }

    public Schrank(int schranknummer, Kunde kunde) {
        this.schranknummer = schranknummer;
        this.kunde = kunde;
    }
    
    public Schrank(){
    }
}
