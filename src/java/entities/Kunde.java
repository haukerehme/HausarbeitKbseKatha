 /*
Copyright [2016] [Katharina Kroener]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Katha
 */
@Entity
public class Kunde implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    private int kundennummer;
    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private String strasse;
    private String ort;
    private String hausnummer;
    private String postleitzahl;
    private String bemerkungen;
    private String vertragsart;
    private int vertragslaufzeit;
    private String telefonnummer;
    private boolean eingecheckt;
    
    public Kunde(){
    }

    public Kunde(int kundennummer, String vorname, String nachname, Date geburtsdatum, String strasse, String ort, String hausnummer, String postleitzahl, String bemerkungen, String vertragsart, int vertragslaufzeit, String telefonnummer, boolean eingecheckt) {
        this.kundennummer = kundennummer;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.strasse = strasse;
        this.ort = ort;
        this.hausnummer = hausnummer;
        this.postleitzahl = postleitzahl;
        this.bemerkungen = bemerkungen;
        this.vertragsart = vertragsart;
        this.vertragslaufzeit = vertragslaufzeit;
        this.telefonnummer = telefonnummer;
        this.eingecheckt = eingecheckt;
    }
    
    /**
     * Vergleicht ein Objekt mit dem Kunden, auf dem die Funktion aufgerufen wird.
     * @param object Es wird ein Objekt übergeben, das verglichen werden soll.
     * @return Gibt zurück, ob das übergebene Objekt gleich dem Objekt ist, auf dem die Methode aufgerufen wurde.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Kunde)) {
            return false;
        }
        Kunde other = (Kunde) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Fügt die Variablen des Kunden zusammen, um sie strukturiert ausgeben zu können.
     * @return Es wird ein String mit den zusammengefügten Variablen zurückgegeben.
     */
    @Override
    public String toString() {
        if(!bemerkungen.equals("") && bemerkungen != null){
            bemerkungen = ", " + bemerkungen;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(geburtsdatum);
        int tag = cal.get(Calendar.DAY_OF_MONTH);
        int monat = cal.get(Calendar.MONTH)+1;
        int jahr = cal.get(Calendar.YEAR);
        return "Kunde: " + kundennummer + ", "
                + vorname + " " + nachname + ", "
                + tag + "." + monat + "." + jahr + ", "
                + strasse + " " + hausnummer + ", " + postleitzahl +" "+ ort + ", Telefon: "
                + telefonnummer + ", "
                + vertragsart + "-Vertrag, " + vertragslaufzeit + " Monate Vertragslaufzeit"
                + bemerkungen;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }
    
    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public String getVertragsart() {
        return vertragsart;
    }

    public void setVertragsart(String vertragsart) {
        this.vertragsart = vertragsart;
    }

    public int getVertragslaufzeit() {
        return vertragslaufzeit;
    }

    public void setVertragslaufzeit(int vertragslaufzeit) {
        this.vertragslaufzeit = vertragslaufzeit;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
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

    public boolean isEingecheckt() {
        return eingecheckt;
    }

    public void setEingecheckt(boolean eingecheckt) {
        this.eingecheckt = eingecheckt;
    }
    
}
