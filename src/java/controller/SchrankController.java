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
import entities.Schrank;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Katha
 */
@Dependent
public class SchrankController implements Serializable{
    
    
    @EJB
    private Persistence persistence;
    
    private int anzahlSchraenke = 100;
    
    
    SchrankController() {
    }
    
    
    
    /**
     * Überprüft, ob bereits Schränke erzeugt wurden und erstellt gegebenenfalls eine vorgegebene Anzahl von Schränken.
     */
    @PostConstruct
    public void initSchrankList(){     
        if(persistence.findAlleSchraenke().isEmpty()){
            for(int i = 0; i < anzahlSchraenke; i++){
                persistence.persistSchrank(new Schrank(i+1));
            }
        }
    }
    
    /**
     * Überprüft, ob es noch einen freien Schrank gibt und sorgt gegebenenfalls dafür, dass nacheinander eincheckende Kunde keine direkt benachbarten Schränke bekommen.
     * @return Gibt die Nummer eines verfügbaren Schrankes zurück
     */
    public int schrankVerfuegbar(){
        int verfuegbarerSchrank = -1;
        List<Schrank> schrankList = persistence.findAlleSchraenke();
        Schrank letzterVergebener = persistence.findLetzterVergebenerSchrank();
        if(letzterVergebener == null){
            return 1;
        }
        for(int i = 0; i < anzahlSchraenke; i++){
            if(schrankList.get(i).getKunde() == null){
                int schrankDiff = letzterVergebener.getSchranknummer() - schrankList.get(i).getSchranknummer();
                if(!(schrankDiff == 1 || schrankDiff == -1)){
                    return schrankList.get(i).getSchranknummer();
                }
                else{
                    verfuegbarerSchrank = schrankList.get(i).getSchranknummer();
                }
            }
        }
        return verfuegbarerSchrank;
    }
    
    /**
     * Überprüft zunächst mithilfe der Funktion schrankVerfuegbar, ob es noch einen leeren Schrank gibt. 
     * Weist einem Kunden gegebenenfalls den verfügbaren Schrank zu, indem die persistence-Funktion schrankZuweisen aufgerufen wird.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, dem ein freier Schrank zugewiesen werden soll.
     * @return Es wird die Schranknummer des verfügbaren Schrankes zurückgegeben.
     */
    public int einchecken(int kundennummer){
        int leererSchrank = schrankVerfuegbar();
        if(leererSchrank != -1){
            Schrank tmpSchrank = persistence.findSchrankBySchranknummer(leererSchrank);
            tmpSchrank.setKunde(persistence.findKundeByKundennummer(kundennummer));
            persistence.schrankZuweisen(kundennummer, tmpSchrank);
        } 
        return leererSchrank;
    }
    
    
    
    /**
     * Weist dem beim Check-Out frei werdenden Schrank null zu und übernimmt diese Änderung mithilfe der persistence-Funktion mergeSchrank in die Datenbank.
     * @param k Es wird der Kunde übergeben, der ausgecheckt werden soll.
     */
    public void auschecken(Kunde k){
        Schrank auscheckSchrank = findSchrankByKundenId(k.getId());
        if(auscheckSchrank != null){
            auscheckSchrank.setKunde(null);
            persistence.mergeSchrank(auscheckSchrank);
        }
        persistence.auschecken(k);
    }
    
    /**
     * Ermittelt einen Schrank anhand der Kunden-ID, indem die gleichnamige persistence-Funktion aufgerufen wird.
     * @param id Es wird die ID des Kunden übergeben, für den der zugewiesene Schrank gefunden werden soll.
     * @return Es wird das Ergebnis der persistence-Funktion zurückgegeben, das den entsprechenden Schrank beinhaltet.
     */
    public Schrank findSchrankByKundenId(long id){
        return persistence.findSchrankByKundenId(id);
    }
    
    /**
     * Ermittelt einen Schrank anhand der Kundennummer, indem die gleichnamige persistence-Funktion aufgerufen wird.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, für den der zugewiesene Schrank gefunden werden soll.
     * @return Es wird das Ergebnis der persistence-Funktion zurückgegeben, das den entsprechenden Schrank beinhaltet.
     */
    public Schrank findSchrankByKundennummer(int kundennummer){
        return persistence.findSchrankByKundennummer(kundennummer);
    }

    public Persistence getKundePersistence() {
        return persistence;
    }

    public void setKundePersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    public int getAnzahlSchraenke() {
        return anzahlSchraenke;
    }

    public void setAnzahlSchraenke(int anzahlSchraenke) {
        this.anzahlSchraenke = anzahlSchraenke;
    }
    
    
}
