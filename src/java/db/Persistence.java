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

    /**
     * Speichert einen neu erstellten Schrank in der Datenbank, indem die EntityManager-Funktion persist aufgerufen wird.
     * @param schrank Es wird der Schrank übergeben, der neu erstellt wurde.
     */
    public void persistSchrank(Schrank schrank){
        manager.persist(schrank);
    }
    
    /**
     * Speichert eine Änderung eines Schrankes in der Datenbank, indem die EntityManager-Funktion merge aufgerufen wird.
     * @param schrank Es wird der Schrank übergeben, dessen Änderungen gespeichert werden sollen.
     */
    public void mergeSchrank(Schrank schrank){
        manager.merge(schrank);
    }
    
    /**
     * Löscht einen Kunden aus der Datenbank, falls dieser nicht eingecheckt ist.
     * @param id Es wird die ID des Kunden übergeben, der gelöscht werden soll.
     * @return Es wird die Information zurückgegeben, ob der Kunde gelöscht werden konnte.
     */
    public String remove(long id){
        Kunde k = findKundeById(id);
        if(k.isEingecheckt() == true){
            return "Der Kunde ist noch eingecheckt. Bitte erst auschecken.";
        }
        else{
            manager.remove(k);
            return "Kunde erfolgreich gelöscht.";
        }
    }
    
    /**
     * Setzt die boolean-Variable eingecheckt auf true und übernimmt die Änderung in die Datenbank.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, der eingecheckt werden soll.
     */
    public void einchecken(int kundennummer){
        Kunde k = findKundeByKundennummer(kundennummer);
        k.setEingecheckt(true);
        manager.merge(k);
    }
    
    /**
     * Setzt die boolean-Variable eingecheckt auf false und übernimmt die Änderung in die Datenbank.
     * @param k Es wird der Kunden übergeben, der eausgecheckt werden soll.
     */
    public void auschecken(Kunde k){  
        k.setEingecheckt(false);
        manager.merge(k);
    }
    
    /**
     * Setzt die boolean-Variable letzterVergebenerSchrank von jedem Schrank auf false und übernimmt die Änderung in die Datenbank.
     */
    public void schraenkeZuruecksetzen(){
        List<Schrank> listSchrank = findAlleSchraenke();
        for(Schrank s : listSchrank){
            s.setLetzterVergebenerSchrank(false);
            mergeSchrank(s);
        }
    }
    
    /**
     * Weist einem Schrank einen Kunden zu, markiert den zugewiesenen Schrank als letzten vergebenen Schrank und übernimmt die Änderungen in die Datenbank.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, dem der Schrank zugewiesen werden soll.
     * @param schrank Es wird der Schrank übergeben, der vergeben werden soll.
     */
    public void schrankZuweisen(int kundennummer, Schrank schrank){ 
        Kunde k = findKundeByKundennummer(kundennummer);
        schrank.setKunde(k);
        schraenkeZuruecksetzen();
        schrank.setLetzterVergebenerSchrank(true);
        mergeSchrank(schrank);
    }
    
    /**
     * Ermittelt alle in der Datenbank existierenden Schränke.
     * @return Es wird eine Liste mit allen in der Datenbank existierenden Schränke zurückgegeben.
     */
    public List<Schrank> findAlleSchraenke(){
        return manager.createQuery("SELECT s FROM Schrank s", Schrank.class).getResultList();
    }
    
    /**
     * Ermittelt alle eingecheckten Kunden in der Datenbank.
     * @return Es wird eine Liste der eingecheckten Kunden zurückgegeben.
     */
    public List<Kunde> findEingecheckteKunden(){
        return manager.createQuery("SELECT k FROM Kunde k WHERE k.eingecheckt = true").getResultList();
    }
    
    /**
     * Ermittelt einen Kunden anhand seiner ID.
     * @param id Es wird die ID des Kunden übergeben, der ermitteln werden soll.
     * @return Es wird der gesuchte Kunde zurückgegeben.
     */
    public Kunde findKundeById(long id){
        Kunde k = manager.find(Kunde.class, id);
        if(k==null){
            return null;
        }
        return k;
    }
    
    
    /**
     * Ermittelt einen Schrank anhand der ID des zugewiesenen Kunden.
     * @param id Es wird die ID des Kunden übergeben, dessen zugewiesener Schrank ermittelt werden soll.
     * @return Es wird der gesuchte Schrank zurückgegeben.
     */
    public Schrank findSchrankByKundenId(long id){
        
        List<Schrank> listSchrank = findAlleSchraenke();
        if(listSchrank.isEmpty()){
            return null;
        }else{
            for(int i = 0; i < listSchrank.size(); i++){
                if(listSchrank.get(i).getKunde() != null){
                    if(listSchrank.get(i).getKunde().getId()==id){
                        return listSchrank.get(i);
                    }
                }
            }
            return null;
        }
    }
    
    /**
     * Ermittelt einen Schrank anhand der Kundennummer des zugewiesenen Kunden.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, dessen zugewiesener Schrank ermittelt werden soll.
     * @return Es wird der gesuchte Schrank zurückgegeben.
     */
    public Schrank findSchrankByKundennummer(int kundennummer){
        
        List<Schrank> listSchrank = findAlleSchraenke();
        if(listSchrank.isEmpty()){
            return null;
        }else{
            for(int i = 0; i < listSchrank.size(); i++){
                if(listSchrank.get(i).getKunde() != null){
                    if(listSchrank.get(i).getKunde().getKundennummer() == kundennummer){
                        return listSchrank.get(i);
                    }
                }
            }
            return null;
        }
    }
    
    
    
    /**
     * Ermittelt den Schrank, der zuletzt vergeben wurde.
     * @return Es wird der zuletzt vergebene Schrank zurückgegeben.
     */
    public Schrank findLetzterVergebenerSchrank(){
        List<Schrank> listSchrank = findAlleSchraenke();
        for(Schrank s : listSchrank){
            if(s.isLetzterVergebenerSchrank()){
                return s;
            }
        }
        return null;
    }
    
    
    /**
     * Ermittelt einen Kunden anhand seiner Kundennummer, falls er existiert.
     * @param kundennummer Es wird die Kundennummer des Kunden übergeben, der gesucht wird.
     * @return Der gesuchte Kunde oder null wird zurückgegeben.
     */
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
    
    /**
     * Ermittelt einen Schrank anhand seiner Schranknummer, falls er existiert.
     * @param schranknummer Es wird die Schranknummer des gesuchten Schranks übergeben.
     * @return Der gesuchte Schrank oder null wird zurückgegeben.
     */
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
    
    /**
     * Ermittelt eine fünfstellige freie Kundennummer, falls noch eine existiert.
     * @return Es wird eine freie Kundennummer zurückgegeben.
     */
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
    
    /**
     * Ermittelt einen Schrank anhand der ID.
     * @param id Es wird die ID des Schranks übergeben, der ermittelt werden soll.
     * @return Der gesuchte Schrank oder null wird zurückgegeben.
     */
    public Schrank findSchrankById(long id){
        Schrank s = manager.find(Schrank.class, id);
        if(s==null){
            return null;
        }
        return s;
    }
    
    
    //getter, setter, Konstruktoren
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
    
}
