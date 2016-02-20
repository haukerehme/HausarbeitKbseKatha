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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Katha
 */
@Entity
public class Schrank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int schranknummer;
    
    @OneToOne
    private Kunde kunde;
    
    public boolean letzterVergebenerSchrank;

    public boolean isLetzterVergebenerSchrank() {
        return letzterVergebenerSchrank;
    }

    public void setLetzterVergebenerSchrank(boolean letzterVergebenerSchrank) {
        this.letzterVergebenerSchrank = letzterVergebenerSchrank;
    }

    public int getSchranknummer() {
        return schranknummer;
    }

    public void setSchranknummer(int schranknummer) {
        this.schranknummer = schranknummer;
    }

    public Schrank(Kunde kunde) {
        this.kunde = kunde;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Schrank)) {
            return false;
        }
        Schrank other = (Schrank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Schrank Nr. " + id + " ]";
    }

    public Schrank(int schranknummer) {
        this.schranknummer = schranknummer;
        this.kunde = null;
        this.letzterVergebenerSchrank = false;
    }
    
    public Schrank(){
    }
}
