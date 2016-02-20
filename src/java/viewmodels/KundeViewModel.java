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

package viewmodels;


import entities.Kunde;
import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import controller.KundeController;

/**
 *
 * @author Katha
 */
@Named(value="kvm")
@SessionScoped
public class KundeViewModel implements Serializable{
    private long id;
    @Min(value=10000, message="Die Kundennummer muss eine fünfstellige Zahl sein.")
    @Max(value=99999, message="Die Kundennummer muss eine fünfstellige Zahl sein.")
    private Integer kundennummer;
    
    
    @Size(min=2, max=30, message="Der Vorname muss 2-30 Zeichen haben")
    @Pattern(regexp="^[A-Z][\\s-a-zA-Zéüäöóß]+$", message="Der Vorname muss einen vorangestellten großen Buchstaben haben. Außerdem sind nur die Zeichen a-z,A-Z,é,ü,ä,ö,ó,- und Leerzeichen erlaubt")
    private String vorname;
    
    
    @Size(min=3, max=50, message="Der Nachname muss 3-50 Zeichen haben")
    @Pattern(regexp="^[A-Z][\\s-a-zA-Zéüäöóß]+$", message="Der Nachname muss einen vorangestellten großen Buchstaben haben. Außerdem sind nur die Zeichen a-z,A-Z,é,ü,ä,ö,ó,- und Leerzeichen erlaubt")
    private String nachname;
    
    @Past(message="Das Geburtsdatum muss in der Vergangenheit liegen.")
    private Date geburtsdatum;
    
    @Size(min=3, max=50, message="Die Straße muss 3-50 Zeichen haben")
    @Pattern(regexp="^[A-Z][\\s-a-zA-Zéüäöóß]+$", message="Die Straße muss einen vorangestellten großen Buchstaben haben. Außerdem sind nur die Zeichen a-z,A-Z,é,ü,ä,ö,ó,- und Leerzeichen erlaubt")
    private String strasse;
    
    @NotNull
    @Size(min=2, max=50, message="Der Ort muss 2-50 Zeichen haben")
    @Pattern(regexp="^[A-Z][\\s-a-zA-Zéüäöóß]+$", message="Der Ort muss einen vorangestellten großen Buchstaben haben. Außerdem sind nur die Zeichen a-z,A-Z,é,ü,ä,ö,ó,- und Leerzeichen erlaubt")
    private String ort;
    
    
    @Size(min=1, max=16, message="Die Hausnummer muss 1-16 Zeichen haben")
    private String hausnummer;

    @Size(min=4, max=7, message="Die Postleitzahl muss 4-7 Zeichen haben")
    private String postleitzahl;
    
    private String bemerkungen;
    
    @Size(min=1, max=30, message="Die Vertragsart muss 1-30 Zeichen haben")
    private String vertragsart;
    
    @Min(value = 1, message="Vertragslaufzeit muss mindestens 1 sein.")
    @Max(value = 36, message="Vertragslaufzeit darf höchstens 36 sein.")
    private int vertragslaufzeit;
    
    @Size(min=4, max=30, message="Die Telefonnummer muss aus 4-30 Zeichen bestehen")
    @Pattern(regexp="^[0-9]+$",message="Für die Telefonnummer bitte nur Zahlen verwenden")
    private String telefonnummer;
    
    
    private boolean eingecheckt;
    private String ausgabeNachricht;
    private String suchErgebnis;
    
    @Inject
    private KundeController kundecontroller;
    
    public KundeViewModel(){
    }
    
    /**
     * Speichert die Einträge der Variablen des gesuchten Kunden mithilfe der Funktion setThisValues in den lokalen Variablen und ruft die kundeController-Funktion kundenSuchErgebnis auf.
     * @param kundennummer Es wird die Kundennummer des gesuchten Kunden übergeben.
     * @return Es wird der String zur Navigation zur Suchergebnis-Seite zurückgegeben.
     */
    public String kundenSuchErgebnis(int kundennummer){
        this.kundennummer = kundennummer;
        if(kundecontroller.findKundeByKundennummer(kundennummer)!=null){
            setThisValues(kundecontroller.findKundeByKundennummer(kundennummer));
        }
        
        this.suchErgebnis = kundecontroller.kundenSuchErgebnis(kundennummer);
        return konstanten.Navigation.SUCHERGEBNIS;
    }
    
    /**
     * Ruft die kundeController-Funktion checkIn auf.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, der eingecheckt werden soll.
     * @return Es wird der String zur Navigation zur Ausgabe-Seite zurückgegeben.
     */
    public String checkIn(int kundennummer){
        this.ausgabeNachricht = kundecontroller.checkIn(kundennummer);
        return konstanten.Navigation.AUSGABE;
    }
    
    /**
     * Ruft die kundeController-Funktion checkOut auf.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, der ausgecheckt werden soll.
     * @return Es wird der String zur Navigation zur Ausgabe-Seite zurückgegeben.
     */
    public String checkOut(int kundennummer){
        this.ausgabeNachricht = kundecontroller.checkOut(kundennummer);
        return konstanten.Navigation.AUSGABE;
    }
    
    /**
     * Speichert die Einträge der Variablen des gesuchten Kunden mithilfe der Funktion setThisValues in den lokalen Variablen.
     * @param id Es wird die ID des Kunden übergeben, der bearbeitet werden soll.
     * @return Es wird der String zur Navigation zur KundenBearbeiten-Seite zurückgegeben.
     */
    public String zurBearbeitung(long id){
        setThisValues(kundecontroller.findKundeById(id));
        return konstanten.Navigation.KUNDENBEARBEITEN;
    }
    
    /**
     * Speichert die Einträge der Variablen des gesuchten Kunden mithilfe der Funktion setThisValues in den lokalen Variablen und ruft die kundeController-Funktion kundenBearbeiten auf.
     * @param id Es wird die ID des Kunden übergeben, der bearbeitet werden soll.
     * @return Es wird der String zur Navigation zur Ausgabe-Seite zurückgegeben.
     */
    public String kundenBearbeiten(long id){
        Kunde k = kundecontroller.findKundeById(id);
        setKundeValues(k);
        kundecontroller.kundenBearbeiten(k);
        ausgabeNachricht = "Änderungen erfolgreich übernommen.";
        zuruecksetzen();
        return konstanten.Navigation.AUSGABE;
    }
    
    /**
     * Ruft die kundeController-Funktion loeschen auf.
     * @param id Es wird die ID des Kunden übergeben, der gelöscht werden soll.
     * @return Es wird ein String zur Navigation zur Ausgabe-Seite zurückgegeben.
     */
    public String loeschen(long id){
        ausgabeNachricht = kundecontroller.loeschen(id);
        zuruecksetzen();
        return konstanten.Navigation.AUSGABE;
    }
    
    /**
     * Erstellt einen neuen Kunden mit einer Kundennummer, die mithilfe der kundeController-Funktion freieKundennummer ermittelt wurde und gibt diesen Kunden an die kundeController-Funktion neuerKunde.
     * @return Es wird ein String zur Navigation zur KundeAngelegt-Seite zurückgegeben.
     */
    public String neuerKunde(){ 
        int freieKundennummer = kundecontroller.freieKundennummer();
        Kunde k = new Kunde(freieKundennummer,vorname,nachname,geburtsdatum,strasse,ort,hausnummer,postleitzahl,bemerkungen,vertragsart,vertragslaufzeit,telefonnummer,false);
        kundecontroller.neuerKunde(k);
        setThisValues(k);
        return konstanten.Navigation.KUNDEANGELEGT;
    }
    
    /**
     * Setzt die lokalen Variablen auf die Werte des übergebenen Kunden.
     * @param k Es wird der Kunde übergeben, dessen Werte in den lokalen Variablen gespeichert werden sollen.
     */
    public void setThisValues(Kunde k){
        id = k.getId();
        kundennummer = k.getKundennummer();
        vorname = k.getVorname();
        nachname = k.getNachname();
        geburtsdatum = k.getGeburtsdatum();
        strasse = k.getStrasse();
        ort = k.getOrt();
        hausnummer = k.getHausnummer();
        postleitzahl = k.getPostleitzahl();
        telefonnummer = k.getTelefonnummer();
        bemerkungen = k.getBemerkungen();
        eingecheckt = k.isEingecheckt();
        vertragsart = k.getVertragsart();
        vertragslaufzeit = k.getVertragslaufzeit();
    }
    
    /**
     * Speichert die Werte der lokalen Variablen in den Variablen des übergebenen Kunden.
     * @param k Es wird der Kunde übergeben, dessen Variablen-Werte geändert werden sollen.
     */
    public void setKundeValues(Kunde k){
        k.setKundennummer(kundennummer);
        k.setVorname(vorname);
        k.setNachname(nachname);
        k.setGeburtsdatum(geburtsdatum);
        k.setStrasse(strasse);
        k.setOrt(ort);
        k.setHausnummer(hausnummer);
        k.setPostleitzahl(postleitzahl);
        k.setTelefonnummer(telefonnummer);
        k.setBemerkungen(bemerkungen);
        k.setEingecheckt(false);
        k.setVertragsart(vertragsart);
        k.setVertragslaufzeit(vertragslaufzeit);
    }
    
    /**
     * Setzt die Werte der lokalen Variablen auf Default-Werte zurück.
     */
    public void zuruecksetzen(){
        kundennummer = 0;
        vorname = "";
        nachname = "";
        geburtsdatum = null;
        strasse = "";
        ort = "";
        hausnummer = "";
        postleitzahl = "";
        telefonnummer = "";
        bemerkungen = "";
        eingecheckt = false;
        vertragsart = "";
        vertragslaufzeit = 0;
    }
    
    
    /**
     * Setzt die lokalen Variablen zurück und navigiert zur Checkin-Seite
     * @return Es wird ein String zur Navigation zur Checkin-Seite zurückgegeben.
     */
    public String zumCheckin(){
        zuruecksetzen();
        return konstanten.Navigation.CHECKIN;
    }
    
    /**
     * Setzt die lokalen Variablen zurück und navigiert zur Checkout-Seite
     * @return Es wird ein String zur Navigation zur Checkout-Seite zurückgegeben.
     */
    public String zumCheckout(){
        zuruecksetzen();
        return konstanten.Navigation.CHECKOUT;
    }
    
    /**
     * Navigiert zur NeuerKunde-Seite.
     * @return Es wird ein String zur Navigation zur NeuerKunde-Seite zurückgegeben.
     */
    public String zumAnlegen(){
        return konstanten.Navigation.NEUERKUNDE;
    }
    
    /**
     * Setzt die lokalen Variablen, überprüft, ob schon Kunden existieren, und navigiert dementsprechend zur Ausgabe- bzw. Kundensuch-Seite
     * @return Es wird ein String zur Navigation zur Ausgabe- bzw. Kundensuch-Seite zurückgegeben.
     */
    public String zurKundensuche(){
        zuruecksetzen();
        if(kundecontroller.findAlleKunden().isEmpty()){
            ausgabeNachricht = "Es existiert noch kein Kunde.";
            return konstanten.Navigation.AUSGABE;
        }
        return konstanten.Navigation.KUNDENSUCHEN;
    }
    
    /**
     * Setzt die lokalen Variablen zurück und navigiert zur Start-Seite
     * @return Es wird ein String zur Navigation zur Start-Seite zurückgegeben.
     */
    public String zurStartseite(){
        zuruecksetzen();
        return konstanten.Navigation.STARTSEITE;
    }

    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }
    
    public KundeController getKundecontroller() {
        return kundecontroller;
    }

    public void setKundecontroller(KundeController kundecontroller) {
        this.kundecontroller = kundecontroller;
    }

    public String getSuchErgebnis() {
        return suchErgebnis;
    }

    public void setSuchErgebnis(String suchErgebnis) {
        this.suchErgebnis = suchErgebnis;
    }

    public String getAusgabeNachricht() {
        return ausgabeNachricht;
    }

    public void setAusgabeNachricht(String ausgabeNachricht) {
        this.ausgabeNachricht = ausgabeNachricht;
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

    public boolean isEingecheckt() {
        return eingecheckt;
    }

    public void setEingecheckt(boolean eingecheckt) {
        this.eingecheckt = eingecheckt;
    }
    
    public long getId(){
        return id;
    }
    
    public void setId(long id){
        this.id = id;
    }
    
}

