/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import db.Persistence;
import entities.Kunde;
import java.io.Serializable;
import java.util.ArrayList;
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

    public void neuerKunde(Kunde kunde){
        persistence.persist(kunde);
    }
    
    public KundeController(){
    }
    
    public void initSchrankController(){
        schrankController.initSchrankList();
    }
    
    public String loeschen(long id){
        return persistence.remove(id);
    }
    
    public List<Kunde> eingecheckteKunden(){
        List<Kunde> ergebnis = new ArrayList<>();
        for(Kunde k:this.persistence.findEingecheckteKunden()){
            ergebnis.add(k);
        }
        return ergebnis;
    }
    
    
    public String checkIn(int kundennummer){
        Kunde kunde = persistence.findKundeByKundennummer(kundennummer);
        if(persistence.findAll().isEmpty()){
            return "Es existiert noch kein Kunde.";
        }
        if(kunde == null){
            return "Es existiert kein Kunde mit der eingegebenen Kundennummer (" + kundennummer + ")";
        }
        else if(kunde.isEingecheckt()){
            return "Kunde ist bereits eingecheckt";
        }
        else{
            int schranknummer = schrankController.einchecken(kundennummer);
            if(schranknummer != -1){
                this.persistence.einchecken(kundennummer);
                return "Kunde erfolgreich eingecheckt. Schranknummer: " + schranknummer;
            }else{
                return "Kein Schrank mehr verfügbar";
            }
        }
    }
    
    public void kundenBearbeiten(Kunde kunde){
        persistence.merge(kunde);
    }
    
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
    
    public Kunde kundenSuchen(int kundennummer){
        return persistence.findKundeByKundennummer(kundennummer);
    }
   
    public String checkOut(int kundennummer){
        
        if(persistence.findAll().isEmpty()){
            return "Es existiert noch kein Kunde.";
        }
        Kunde k = persistence.findKundeByKundennummer(kundennummer);
        if(k == null){
            return "Es existiert kein Kunde mit der eingegebenen Kundennummer (" + kundennummer + ")";
        }
        else if(!k.isEingecheckt()){
            return "Kunde ist nicht eingecheckt.";
        }
        else{
            persistence.auschecken(k);
            return "Kunde erfolgreich ausgecheckt";
        }
    }

    public Persistence getPersistence() {
        return persistence;
    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }
    
    public int freieKundennummer(){
        return persistence.findFreieKundennummer();
    }
    
}
