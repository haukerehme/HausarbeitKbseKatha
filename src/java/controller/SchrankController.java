 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
   // private List<Schrank> schrankList;
    
    @EJB
    private Persistence persistence;
    
    
    private int letzterVergebenerSchrank = -1;
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
                persistence.persistSchrank(new Schrank(i+1,null));
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
        for(int i = 0; i < anzahlSchraenke; i++){
            if(letzterVergebenerSchrank == -1){
                return 1;
            }
            if(schrankList.get(i).getKunde() == null){
                int schrankDiff = letzterVergebenerSchrank - schrankList.get(i).getSchranknummer();
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
            letzterVergebenerSchrank = leererSchrank;
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
        if(!(auscheckSchrank == null)){
            persistence.findAlleSchraenke().get(auscheckSchrank.getId().intValue()).setKunde(null);
        }
        if(persistence.findEingecheckteKunden().isEmpty()){
            letzterVergebenerSchrank = -1;
        }
    }
    
    /**
     *
     * @param id
     * @return
     */
    public Schrank findSchrankByKundenId(long id){
        for(Schrank s : persistence.findAlleSchraenke()){
            if(s.getKunde().getId() == id){
                return s;
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

    public int getLetzterVergebenerSchrank() {
        return letzterVergebenerSchrank;
    }

    public void setLetzterVergebenerSchrank(int letzterVergebenerSchrank) {
        this.letzterVergebenerSchrank = letzterVergebenerSchrank;
    }

    public int getAnzahlSchraenke() {
        return anzahlSchraenke;
    }

    public void setAnzahlSchraenke(int anzahlSchraenke) {
        this.anzahlSchraenke = anzahlSchraenke;
    }
    
    
}
