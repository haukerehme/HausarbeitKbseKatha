/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import entities.Kunde;
import entities.Schrank;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Katha
 */
@Stateless
public class Persistence extends AbstractPersistence<Kunde> {
    
    @Inject
    private EntityManager manager;

    @Override
    public EntityManager getEntityManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }
    
    public Persistence() {
        super(Kunde.class);
    }
    
    public void persistSchrank(Schrank schrank){
        manager.persist(schrank);
    }
    
    public void mergeSchrank(Schrank schrank){
        manager.merge(schrank);
    }
    
    public List<Schrank> findAlleSchraenke(){
        return manager.createQuery("SELECT s FROM Schrank s", Schrank.class).getResultList();
    }
    /*
    public Object merge(Object object){
        return manager.merge(object);
    }*/
    
    public List<Kunde> findEingecheckteKunden(){
        return manager.createQuery("SELECT k FROM Kunde k WHERE k.eingecheckt = true").getResultList();
    }
    
    public Kunde findKundeById(long id){
        Kunde k = manager.find(Kunde.class, id);
        if(k==null){
            return null;
        }
        return k;
    }
    
    public void einchecken(int kundennummer){
        Kunde k = findKundeByKundennummer(kundennummer);
        k.setEingecheckt(true);
        manager.merge(k);
    }
    
    public Schrank findSchrankByKundenId(long id){
        return (Schrank) manager.createQuery("SELECT s FROM Schrank s WHERE s.kunde.id := id").getSingleResult();
    }
    
    public void auschecken(Kunde k){  
        List<Schrank> allSchraenke = this.findAlleSchraenke();
        Kunde tmp;
        for(int i=0;i<allSchraenke.size();i++){
            tmp = allSchraenke.get(i).getKunde();
            if(tmp != null){
                if(allSchraenke.get(i).getKunde().equals(k)){
                    allSchraenke.get(i).setKunde(null);
                    this.mergeSchrank(allSchraenke.get(i));
                    
                    break;
                }
            }
        }
        k.setEingecheckt(false);
        manager.merge(k);
    }
    
    /*public List<Kunde> findAlleKunden(){
        return manager.createQuery("SELECT k FROM Kunde k", Kunde.class).getResultList();
    }*/
    
    public void schrankZuweisen(int kundennummer, Schrank schrank){ 
        Kunde k = findKundeByKundennummer(kundennummer);
        schrank.setKunde(k);
        manager.merge(schrank);
    }
    
    public Kunde findKundeByKundennummer(int kundennummer){
        List<Kunde> listKunde = findAll();
        if(listKunde.isEmpty()){
            return null;
        }else{
            for(int i = 0; i < listKunde.size(); i++){
                if(listKunde.get(i).getKundennummer()==kundennummer){
                    return listKunde.get(i);
                }
            }
            return null;
        }
        
        /*if((Kunde) manager.createQuery("SELECT k FROM Kunde k WHERE k.kundennummer := kundennummer").getSingleResult() !=null){
            return (Kunde) manager.createQuery("SELECT k FROM Kunde k WHERE k.kundennummer := kundennummer").getSingleResult();
        }
        else{
            return null;
        }*/
        
    }
    
    public Schrank findSchrankBySchranknummer(int schranknummer){
        List<Schrank> listSchrank = findAlleSchraenke();
        if(listSchrank.isEmpty()){
            return null;
        }else{
            for(int i = 0; i < listSchrank.size(); i++){
                if(listSchrank.get(i).getSchranknummer()==schranknummer){
                    return listSchrank.get(i);
                }
            }
            return null;
        }
    }
    
    public int findFreieKundennummer(){
        int freieNummer = -1;
        boolean frei;
        List<Kunde> alleKunden = this.findAll();
        for(int i = 10000; i < 99999; i++){
            frei = true;
            for(int j = 0; j < alleKunden.size(); j++){
                if((alleKunden.get(j).getKundennummer() == i)){
                    frei = false;
                }
            }
            if(frei){
                freieNummer = i;
                break;
            }
        }
        return freieNummer;
    }
    
    public Schrank findSchrankById(long id){
        Schrank s = manager.find(Schrank.class, id);
        if(s==null){
            return null;
        }
        return s;
    }
    
    public String remove(long id){
        Kunde k = findKundeById(id);
        if(k.isEingecheckt() == true){
            return "Der Kunde ist noch eingecheckt. Bitte erst auschecken.";
        }
        else{
            remove(k);
            return "Kunde erfolgreich gelöscht.";
        }
    }

}
