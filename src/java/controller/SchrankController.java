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
     * Initialisiert die Schränke
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
     *
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
     *
     * @param kundennummer
     * @return
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
     *
     * @param id
     */
    public void auschecken(long id){
        Schrank auscheckSchrank = findSchrankByKundenId(id);
        if(auscheckSchrank != null){
            auscheckSchrank.setKunde(null);
            persistence.mergeSchrank(auscheckSchrank);
        }
    }
    
    /**
     *
     * @param id
     * @return
     */
    public Schrank findSchrankByKundenId(long id){
        for(Schrank s : persistence.findAlleSchraenke()){
            if(s.getKunde() != null){
                if(s.getKunde().getId() == id){
                    return s;
                }
            }
        }
        return null;
    }
    
    public Schrank findSchrankByKundennummer(int kundennummer){
        List<Schrank> listSchrank = persistence.findAlleSchraenke();
        for(Schrank s : listSchrank){
            if(s.getKunde() != null){
                if(s.getKunde().getKundennummer() == kundennummer){
                    return s;
                }
            }
        }
        return null;
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
