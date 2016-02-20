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

import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Katha
 * @param <T> Platzhalter für Klassen
 */
public abstract class AbstractPersistence<T> {
    private Class<T> entityClass;

    
    public AbstractPersistence(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Persistiert ein Objekt der Klasse T.
     * @param entity Das zu persistierende Objekt wird übergeben.
     */
    public void persist(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Speichert den Stand eines Objekts der Klasse T.
     * @param entity Das Objekt, dessen Stand gespeichert werden soll, wird übergeben.
     */
    public void merge(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Löscht ein Objekt der Klasse T.
     * @param entity Das zu löschende Objekt wird übergeben.
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Ermittelt ein Objekt anhand seiner ID.
     * @param id Die ID des gesuchten Objekts wird übergeben.
     * @return Das gesuchte Objekt wird zurückgegeben.
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Ermittelt alle existierenden Objekte der Klasse T.
     * @return Es wird eine Liste aller Objekte der Klasse T zurückgegeben.
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }    
}

