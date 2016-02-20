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

package db;

import entities.Kunde;
import entities.Schrank;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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
        
        List<Schrank> listSchrank = findAlleSchraenke();
        if(listSchrank.isEmpty()){
            return null;
        }else{
            for(int i = 0; i < listSchrank.size(); i++){
                    if(listSchrank.get(i).getId()==id){
                        return listSchrank.get(i);
                    }
            }
            return null;
        }
    }
    
    public void auschecken(Kunde k){  
        List<Schrank> allSchraenke = findAlleSchraenke();
        Kunde tmp;
        for(int i=0;i<allSchraenke.size();i++){
            tmp = allSchraenke.get(i).getKunde();
            if(tmp != null){
                if(allSchraenke.get(i).getKunde().equals(k)){
                    allSchraenke.get(i).setKunde(null);
                    mergeSchrank(allSchraenke.get(i));
                    
                    break;
                }
            }
        }
        k.setEingecheckt(false);
        manager.merge(k);
    }
    
    public void schrankZuweisen(int kundennummer, Schrank schrank){ 
        Kunde k = findKundeByKundennummer(kundennummer);
        schrank.setKunde(k);
        schraenkeZuruecksetzen();
        schrank.setLetzterVergebenerSchrank(true);
        manager.merge(schrank);
    }
    
    public Schrank findLetzterVergebenerSchrank(){
        List<Schrank> listSchrank = this.findAlleSchraenke();
        for(Schrank s : listSchrank){
            if(s.isLetzterVergebenerSchrank()){
                return s;
            }
        }
        return null;
    }
    
    public void schraenkeZuruecksetzen(){
        List<Schrank> listSchrank = findAlleSchraenke();
        for(Schrank s : listSchrank){
            s.setLetzterVergebenerSchrank(false);
            mergeSchrank(s);
        }
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
