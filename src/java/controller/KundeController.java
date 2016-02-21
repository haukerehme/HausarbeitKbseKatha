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

package controller;

import db.Persistence;
import entities.Kunde;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author Katha
 */
@Dependent
public class KundeController implements Serializable{
    @EJB
    private Persistence persistence;
    
    @Inject
    SchrankController schrankController;

    /**
     * Default-Konstruktor
     */
    public KundeController(){
    }
    
    
    /**
     * Erstellt einen neuen Kunden, indem die persistence-Funktion persist aufgerufen wird.
     * @param kunde Es wird der Kunde übergeben, der erstellt werden soll.
     */
    public void neuerKunde(Kunde kunde){
        persistence.persist(kunde);
    }
    
    /**
     * Löscht einen vorhandenen Kunden, indem die peristence-Funktion remove aufgerufen wird.
     * @param id Es wird die ID des Kunden übergeben, der gelöscht werden soll.
     * @return Es wird der Rückgabe-Text der Funktion remove(id) zurückgegeben.
     */
    public String loeschen(long id){
        return persistence.remove(id);
    }
    
    /**
     * Ruft zur Speicherung der Bearbeitung eines Kunden die persistence-Funktion merge auf.
     * @param kunde Es wird der Kunde übergeben, der bearbeitet werden soll.
     */
    public void kundenBearbeiten(Kunde kunde){
        persistence.merge(kunde);
    }
    
    /**
     * Ermittelt mithilfe der persistence-Funktion findKundeByKundennummer, ob der gesuchte Kunde existiert und ruft gegebenenfalls die to-String-Methode auf.
     * @param kundennummer Es wird die Kundennummer des gesuchten Kunden übergeben.
     * @return Es wird eine Nachricht zurückgegeben, die die Daten des Kunden beinhaltet, falls der Kunde existiert. Andernfalls wird die Nachricht, dass der Kunde nicht existiert, zurückgegeben.
     */
    public String kundenSuchErgebnis(int kundennummer){
        if(persistence.findAll().isEmpty()){
            return "Es existiert noch kein Kunde.";
        }
        else if(persistence.findKundeByKundennummer(kundennummer) == null){
            return "Es existiert kein Kunde mit der eingegebenen Kundennummer (" + kundennummer + ")";
        }
        else{
            return persistence.findKundeByKundennummer(kundennummer).toString();
        }
    }
    
    /**
     * Überprüft, ob ein Kunde eingecheckt werden kann und ruft gegebenenfalls die persistence-Funktion einchecken auf.
     * @param kundennummer Es s wird eine Nachricht zurückgegeben, in der das Ergebnwird die Kundennummer des Kunden übergeben, der eingecheckt werden soll.
     * @return Es wird eine Nachricht zurückgegeben, in der das Ergebnis des Eincheckens beschrieben wird.
     */
    public String checkIn(int kundennummer){
        Kunde kunde = persistence.findKundeByKundennummer(kundennummer);
        if(persistence.findAll().isEmpty()){
            return "Es existiert noch kein Kunde.";
        }
        if(kunde == null){
            return "Es existiert kein Kunde mit der eingegebenen Kundennummer (" + kundennummer + ")";
        }
        else if(kunde.isEingecheckt() && schrankController.findSchrankByKundennummer(kundennummer) != null){
            return "Kunde ist bereits eingecheckt";
        }
        else{
            int schranknummer = schrankController.einchecken(kundennummer);
            if(schranknummer != -1){
                persistence.einchecken(kundennummer);
                return "Kunde erfolgreich eingecheckt. Schranknummer: " + schranknummer;
            }else{
                return "Kein Schrank mehr verfügbar";
            }
        }
    }
   
    /**
     * Überprüft, ob ein Kunde ausgecheckt werden kann und ruft gegebenenfalls die persistence-Funktion auschecken auf.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, der ausgecheckt werden soll.
     * @return Es wird eine Nachricht zurückgegeben, in der das Ergebnis des Auscheckens beschrieben wird.
     */
    public String checkOut(int kundennummer){
        if(persistence.findAll().isEmpty()){
            return "Es existiert noch kein Kunde.";
        }
        Kunde k = persistence.findKundeByKundennummer(kundennummer);
        if(k == null){
            return "Es existiert kein Kunde mit der eingegebenen Kundennummer (" + kundennummer + ")";
        }
        else if(!k.isEingecheckt() && schrankController.findSchrankByKundennummer(kundennummer)==null){
            return "Kunde ist nicht eingecheckt.";
        }
        else{
            persistence.auschecken(k);
            schrankController.auschecken(k);
            return "Kunde erfolgreich ausgecheckt";
        }
    }
    
    /**
     * Ermittelt alle Kunden, die momentan eingecheckt sind, indem die persistence-Funktion findEingecheckteKunden aufgerufen wird.
     * @return Es wird das Ergebnis der persistence-Funktion findEingecheckteKunden zurückgegeben, das alle Kunden, die momentan eingecheckt sind, beinhaltet.
     */
    public List<Kunde> eingecheckteKunden(){
        return persistence.findEingecheckteKunden();
    }
    
    /**
     * Ermittelt einen Kunden anhand seiner Kundennummer, indem die persistence-Funktion findKundeByKundennummer aufgerufen wird.
     * @param kundennummer Es wird die Kundennummer übergeben, für die der entsprechende Kunde ermittelt werden soll.
     * @return Es wird das Ergebnis der persistence-Funktion findKundeByKundennummer zurückgegeben, die den gesuchten Kunden beinhaltet.
     */
    public Kunde findKundeByKundennummer(int kundennummer){
        return persistence.findKundeByKundennummer(kundennummer);
    }
    
    /**
     * Ermittelt einen Kunden anhand seiner ID, indem die persistence-Funktion findKundeByKundennummer aufgerufen wird.
     * @param id Es wird die ID übergeben, für die der entsprechende Kunde ermittelt werden soll.
     * @return Es wird das Ergebnis der persistence-Funktion findKundeByKundennummer zurückgegeben, die den gesuchten Kunden beinhaltet.
     */
    public Kunde findKundeById(long id){
        return persistence.findKundeById(id);
    }
    
    /**
     * Ermittelt alle existierenden Kunden, indem die persistence-Funktion findAll aufgerufen wird.
     * @return Es wird der Rückgabewert der persistence-Funktion findAll zurückgegeben, der einer Liste aller Kunden entspricht.
     */
    public List<Kunde> findAlleKunden(){
        return persistence.findAll();
    }
    
    /**
     * Ermittelt eine freie Kundennummer, indem die persistence-Funktion findFreieKundennummer aufgerufen wird.
     * @return Es wird der Rückgabewert der persistence-Funktion findFreieKundennummer zurückgegeben, der eine freie Kundennummer beinhaltet.
     */
    public int freieKundennummer(){
        return persistence.findFreieKundennummer();
    }

    public Persistence getPersistence() {
        return persistence;
    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }
    
}
